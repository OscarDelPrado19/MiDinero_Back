package com.midinero.service;

import com.midinero.dto.ApiResponse;
import com.midinero.dto.CambiarPasswordDTO;
import com.midinero.dto.UsuarioResponseDTO;
import com.midinero.entity.Usuario;
import com.midinero.repository.UsuarioRepository;
import com.midinero.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ApiResponse<UsuarioResponseDTO> obtenerPerfilUsuario() {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            UsuarioResponseDTO responseDTO = convertirAUsuarioResponseDTO(usuario);
            return ApiResponse.success("Perfil obtenido exitosamente", responseDTO);
        } catch (Exception e) {
            return ApiResponse.error("Error al obtener el perfil");
        }
    }

    public ApiResponse<String> cambiarPassword(CambiarPasswordDTO cambiarPasswordDTO) {
        // Validar que las contraseñas coincidan
        if (!cambiarPasswordDTO.getNuevaPassword().equals(cambiarPasswordDTO.getConfirmarPassword())) {
            return ApiResponse.error("Las contraseñas no coinciden");
        }

        try {
            Usuario usuario = obtenerUsuarioAutenticado();

            // Verificar contraseña actual
            if (!passwordEncoder.matches(cambiarPasswordDTO.getPasswordActual(), usuario.getPassword())) {
                return ApiResponse.error("La contraseña actual es incorrecta");
            }

            // Actualizar contraseña
            usuario.setPassword(passwordEncoder.encode(cambiarPasswordDTO.getNuevaPassword()));
            usuarioRepository.save(usuario);

            return ApiResponse.success("Contraseña actualizada exitosamente");
        } catch (Exception e) {
            return ApiResponse.error("Error al cambiar la contraseña");
        }
    }

    public ApiResponse<String> eliminarCuenta() {
        try {
            Usuario usuario = obtenerUsuarioAutenticado();
            usuarioRepository.delete(usuario);
            return ApiResponse.success("Cuenta eliminada exitosamente");
        } catch (Exception e) {
            return ApiResponse.error("Error al eliminar la cuenta");
        }
    }

    private Usuario obtenerUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return usuarioRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    private UsuarioResponseDTO convertirAUsuarioResponseDTO(Usuario usuario) {
        return new UsuarioResponseDTO(
            usuario.getId(),
            usuario.getNombreCompleto(),
            usuario.getEmail(),
            usuario.getCarrera(),
            usuario.getFechaNacimiento(),
            usuario.getFechaRegistro()
        );
    }
}
