package com.example.demo4.repository;

import com.example.demo4.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByRol(String rol);
    List<Usuario> findByActivoTrue();
    boolean existsByEmail(String email);
}
