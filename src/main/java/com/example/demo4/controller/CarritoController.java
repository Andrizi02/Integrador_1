package com.example.demo4.controller;

import com.example.demo4.model.Carrito;
import com.example.demo4.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Carrito>> obtener(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(idUsuario));
    }

    @PostMapping
    public ResponseEntity<Carrito> agregar(@RequestBody Map<String, Object> datos) {
        Long idUsuario = Long.valueOf(datos.get("idUsuario").toString());
        Long idProducto = Long.valueOf(datos.get("idProducto").toString());
        Integer cantidad = Integer.valueOf(datos.getOrDefault("cantidad", "1").toString());
        return ResponseEntity.ok(carritoService.agregarProducto(idUsuario, idProducto, cantidad));
    }

    @PutMapping("/{idCarrito}")
    public ResponseEntity<Carrito> actualizar(
            @PathVariable Long idCarrito,
            @RequestBody Map<String, Integer> datos) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(idCarrito, datos.get("cantidad")));
    }

    @DeleteMapping("/{idCarrito}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idCarrito) {
        carritoService.eliminarProducto(idCarrito);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/usuario/{idUsuario}")
    public ResponseEntity<Void> vaciar(@PathVariable Long idUsuario) {
        carritoService.vaciarCarrito(idUsuario);
        return ResponseEntity.noContent().build();
    }
}