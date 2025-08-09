package com.midinero.service;

import com.midinero.dto.ApiResponse;
import com.midinero.dto.TransaccionDTO;
import com.midinero.entity.Transaccion;
import com.midinero.entity.Usuario;
import com.midinero.repository.TransaccionRepository;
import com.midinero.repository.UsuarioRepository;
import com.midinero.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransaccionService {

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PresupuestoService presupuestoService;

    @Autowired
    private MetaAhorroService metaAhorroService;

    public ApiResponse<List<TransaccionDTO>> obtenerTransacciones() {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            List<Transaccion> transacciones = transaccionRepository.findByUsuarioIdOrderByFechaDesc(usuario.getId());
            
            List<TransaccionDTO> transaccionesDTO = transacciones.stream()
                .map(this::convertirATransaccionDTO)
                .collect(Collectors.toList());

            return ApiResponse.success("Transacciones obtenidas exitosamente", transaccionesDTO);
        } catch (Exception e) {
            return ApiResponse.error("Error al obtener las transacciones");
        }
    }

    public ApiResponse<TransaccionDTO> crearTransaccion(TransaccionDTO transaccionDTO) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();

            Transaccion transaccion = new Transaccion();
            transaccion.setUsuario(usuario);
            transaccion.setTipo(transaccionDTO.getTipo());
            transaccion.setCategoria(transaccionDTO.getCategoria());
            transaccion.setMonto(transaccionDTO.getMonto());
            transaccion.setDescripcion(transaccionDTO.getDescripcion());

            Transaccion transaccionGuardada = transaccionRepository.save(transaccion);

            // Verificar presupuestos si es un gasto
            if (transaccion.getTipo() == Transaccion.TipoTransaccion.GASTO) {
                presupuestoService.verificarPresupuesto(usuario.getId(), transaccion.getCategoria(), transaccion.getMonto());
            }

            // Actualizar metas de ahorro si es un ingreso
            if (transaccion.getTipo() == Transaccion.TipoTransaccion.INGRESO) {
                metaAhorroService.actualizarMetasConIngreso(usuario.getId(), transaccion.getMonto());
            }

            TransaccionDTO responseDTO = convertirATransaccionDTO(transaccionGuardada);
            return ApiResponse.success("Transacción creada exitosamente", responseDTO);
        } catch (Exception e) {
            return ApiResponse.error("Error al crear la transacción");
        }
    }

    public ApiResponse<TransaccionDTO> actualizarTransaccion(Long id, TransaccionDTO transaccionDTO) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            
            Transaccion transaccion = transaccionRepository.findById(id)
                .orElse(null);

            if (transaccion == null || !transaccion.getUsuario().getId().equals(usuario.getId())) {
                return ApiResponse.error("Transacción no encontrada");
            }

            transaccion.setTipo(transaccionDTO.getTipo());
            transaccion.setCategoria(transaccionDTO.getCategoria());
            transaccion.setMonto(transaccionDTO.getMonto());
            transaccion.setDescripcion(transaccionDTO.getDescripcion());

            Transaccion transaccionActualizada = transaccionRepository.save(transaccion);
            TransaccionDTO responseDTO = convertirATransaccionDTO(transaccionActualizada);

            return ApiResponse.success("Transacción actualizada exitosamente", responseDTO);
        } catch (Exception e) {
            return ApiResponse.error("Error al actualizar la transacción");
        }
    }

    public ApiResponse<String> eliminarTransaccion(Long id) {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            
            Transaccion transaccion = transaccionRepository.findById(id)
                .orElse(null);

            if (transaccion == null || !transaccion.getUsuario().getId().equals(usuario.getId())) {
                return ApiResponse.error("Transacción no encontrada");
            }

            transaccionRepository.delete(transaccion);
            return ApiResponse.success("Transacción eliminada exitosamente");
        } catch (Exception e) {
            return ApiResponse.error("Error al eliminar la transacción");
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return usuarioRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private TransaccionDTO convertirATransaccionDTO(Transaccion transaccion) {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setId(transaccion.getId());
        dto.setTipo(transaccion.getTipo());
        dto.setCategoria(transaccion.getCategoria());
        dto.setMonto(transaccion.getMonto());
        dto.setDescripcion(transaccion.getDescripcion());
        dto.setFecha(transaccion.getFecha());
        return dto;
    }
}
