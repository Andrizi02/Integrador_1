package com.example.demo4.controller;

import com.example.demo4.model.Pedido;
import com.example.demo4.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping
    public ResponseEntity<List<Pedido>> listar() {
        return ResponseEntity.ok(pedidoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Pedido>> porUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.porUsuario(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Pedido> crear(@RequestBody Map<String, Object> datos) {
        Long idComprador = Long.valueOf(datos.get("idComprador").toString());
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) datos.get("items");
        return ResponseEntity.ok(pedidoService.crearPedido(idComprador, items));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Pedido> actualizarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, String> datos) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, datos.get("estado")));
    }
}
