package com.example.demo4.service;

import com.example.demo4.model.*;
import com.example.demo4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Carrito> obtenerCarrito(Long idUsuario) {
        return carritoRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public Carrito agregarProducto(Long idUsuario, Long idProducto, Integer cantidad) {
        Optional<Carrito> existente = carritoRepository
                .findByUsuarioIdUsuarioAndProductoIdProducto(idUsuario, idProducto);

        if (existente.isPresent()) {
            Carrito item = existente.get();
            item.setCantidad(item.getCantidad() + cantidad);
            return carritoRepository.save(item);
        }

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Carrito nuevo = new Carrito();
        nuevo.setUsuario(usuario);
        nuevo.setProducto(producto);
        nuevo.setCantidad(cantidad);
        return carritoRepository.save(nuevo);
    }

    public Carrito actualizarCantidad(Long idCarrito, Integer cantidad) {
        Carrito item = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Item no encontrado"));
        if (cantidad < 1) {
            carritoRepository.delete(item);
            return null;
        }
        item.setCantidad(cantidad);
        return carritoRepository.save(item);
    }

    public void eliminarProducto(Long idCarrito) {
        carritoRepository.deleteById(idCarrito);
    }

    public void vaciarCarrito(Long idUsuario) {
        carritoRepository.deleteByUsuarioIdUsuario(idUsuario);
    }
}
