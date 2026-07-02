package com.example.demo4.service;

import com.example.demo4.model.Producto;
import com.example.demo4.repository.ProductoRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service

public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

     @Transactional
    public List<Producto> listallProductos() {
        return productoRepository.findAll();
    }


    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> buscar(String busqueda) {
        return productoRepository.buscarProductos(busqueda);
    }

    public List<Producto> porCategoria(String categoria) {
        return productoRepository.findByCategoriaNombre(categoria);
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizar(Long id, Producto productoActualizado) {
        Producto producto = obtenerPorId(id);
        producto.setNombre(productoActualizado.getNombre());
        producto.setDescripcion(productoActualizado.getDescripcion());
        producto.setPrecioUnitario(productoActualizado.getPrecioUnitario());
        producto.setCantidadDisponible(productoActualizado.getCantidadDisponible());
        producto.setUbicacion(productoActualizado.getUbicacion());
        return productoRepository.save(producto);
    }

    public void eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        producto.setActivo(false);
        productoRepository.save(producto);
    }
}
