package com.example.demo4.service;

import com.example.demo4.model.Usuario;
import com.example.demo4.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.*;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarActivos() {
        return usuarioRepository.findByActivoTrue();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public List<Usuario> porRol(String rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Usuario registrar(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        usuario.setPasswordHash(hashPassword(usuario.getPasswordHash()));
        usuario.setActivo(true);
        usuario.setFechaRegistro(java.time.LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    public Map<String, Object> login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));
        
        if (!usuario.getPasswordHash().equals(hashPassword(password))) {
            throw new RuntimeException("Credenciales inválidas");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", usuario.getIdUsuario());
        response.put("nombre", usuario.getNombre());
        response.put("email", usuario.getEmail());
        response.put("rol", usuario.getRol());
        response.put("token", generarToken());
        return response;
    }

    public Usuario actualizar(Long id, Usuario datos) {
        Usuario usuario = obtenerPorId(id);
        usuario.setNombre(datos.getNombre());
        usuario.setApellido(datos.getApellido());
        usuario.setTelefono(datos.getTelefono());
        usuario.setUbicacion(datos.getUbicacion());
        if (datos.getPasswordHash() != null && !datos.getPasswordHash().isEmpty()) {
            usuario.setPasswordHash(hashPassword(datos.getPasswordHash()));
        }
        return usuarioRepository.save(usuario);
    }

    public void desactivar(Long id) {
        Usuario usuario = obtenerPorId(id);
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar contraseña");
        }
    }

    private String generarToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
