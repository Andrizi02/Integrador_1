package com.example.demo4.repository;


import com.example.demo4.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByProductoIdProducto(Long idProducto);
    List<Resena> findByCompradorIdUsuario(Long idComprador);
}
