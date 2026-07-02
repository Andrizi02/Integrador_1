package com.example.demo4.service;


import com.example.demo4.model.*;
import com.example.demo4.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido obtenerPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
    }

    public List<Pedido> porUsuario(Long idUsuario) {
        return pedidoRepository.findByCompradorIdUsuarioOrderByFechaPedidoDesc(idUsuario);
    }

    @Transactional
    public Pedido crearPedido(Long idComprador, List<Map<String, Object>> items) {
        Usuario comprador = usuarioRepository.findById(idComprador)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setComprador(comprador);
        pedido.setEstado("pendiente");
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setFechaActualizacion(LocalDateTime.now());
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        BigDecimal total = BigDecimal.ZERO;
        List<DetallePedido> detalles = new ArrayList<>();

        for (Map<String, Object> item : items) {
            Long idProducto = Long.valueOf(item.get("idProducto").toString());
            Integer cantidad = Integer.valueOf(item.get("cantidad").toString());

            Producto producto = productoRepository.findById(idProducto)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (producto.getCantidadDisponible() < cantidad) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedidoGuardado);
            detalle.setProducto(producto);
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecioUnitario());
            detalles.add(detalle);

            total = total.add(producto.getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad)));
        }

        pedidoGuardado.setDetalles(detalles);
        pedidoGuardado.setTotal(total);
        pedidoGuardado = pedidoRepository.save(pedidoGuardado);

        carritoRepository.deleteByUsuarioIdUsuario(idComprador);

        return pedidoGuardado;
    }

    public Pedido actualizarEstado(Long id, String nuevoEstado) {
        Pedido pedido = obtenerPorId(id);
        pedido.setEstado(nuevoEstado);
        pedido.setFechaActualizacion(LocalDateTime.now());
        return pedidoRepository.save(pedido);
    }
}
