package com.example.demo4.repository;

import com.example.demo4.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    List<Carrito> findByUsuarioIdUsuario(Long idUsuario);
    Optional<Carrito> findByUsuarioIdUsuarioAndProductoIdProducto(Long idUsuario, Long idProducto);
    void deleteByUsuarioIdUsuario(Long idUsuario);
}
