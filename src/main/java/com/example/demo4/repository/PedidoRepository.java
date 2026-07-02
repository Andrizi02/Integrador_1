package com.example.demo4.repository;


import com.example.demo4.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByCompradorIdUsuarioOrderByFechaPedidoDesc(Long idComprador);
    List<Pedido> findByEstado(String estado);
}
