package com.midinero.controller;

import com.midinero.dto.ApiResponse;
import com.midinero.dto.CambiarPasswordDTO;
import com.midinero.dto.UsuarioResponseDTO;
import com.midinero.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UsuarioResponseDTO>> obtenerPerfil() {
        ApiResponse<UsuarioResponseDTO> response = usuarioService.obtenerPerfilUsuario();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/cambiar-password")
    public ResponseEntity<ApiResponse<String>> cambiarPassword(@Valid @RequestBody CambiarPasswordDTO cambiarPasswordDTO) {
        ApiResponse<String> response = usuarioService.cambiarPassword(cambiarPasswordDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/eliminar-cuenta")
    public ResponseEntity<ApiResponse<String>> eliminarCuenta() {
        ApiResponse<String> response = usuarioService.eliminarCuenta();
        return ResponseEntity.ok(response);
    }
}
