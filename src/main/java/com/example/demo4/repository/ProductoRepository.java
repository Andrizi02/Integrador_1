package com.example.demo4.repository;


import com.example.demo4.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    
    List<Producto> findByCategoriaNombre(String categoria);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND " +
           "(LOWER(p.nombre) LIKE LOWER(CONCAT('%', :busqueda, '%')) OR " +
           "LOWER(p.ubicacion) LIKE LOWER(CONCAT('%', :busqueda, '%')))")
    List<Producto> buscarProductos(String busqueda);
    
    List<Producto> findByProductorIdUsuario(Long idProductor);
    
    @Query("SELECT p FROM Producto p WHERE p.activo = true ORDER BY p.precioUnitario ASC")
    List<Producto> findAllOrderByPrecio();
}
