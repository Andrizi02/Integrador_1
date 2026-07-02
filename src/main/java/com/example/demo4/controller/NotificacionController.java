package com.example.demo4.controller;

import com.example.demo4.model.Notificacion;
import com.example.demo4.service.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<Notificacion>> obtener(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.obtenerNotificaciones(idUsuario));
    }

    @GetMapping("/{idUsuario}/no-leidas")
    public ResponseEntity<Long> contarNoLeidas(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(notificacionService.contarNoLeidas(idUsuario));
    }

    @PutMapping("/{id}/leer")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        notificacionService.marcarLeida(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{idUsuario}/leer-todas")
    public ResponseEntity<Void> marcarTodasLeidas(@PathVariable Long idUsuario) {
        notificacionService.marcarTodasLeidas(idUsuario);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Map<String, Object> datos) {
        Long idUsuario = Long.valueOf(datos.get("idUsuario").toString());
        String mensaje = datos.get("mensaje").toString();
        String tipo = datos.getOrDefault("tipo", "info").toString();
        return ResponseEntity.ok(notificacionService.crearNotificacion(idUsuario, mensaje, tipo));
    }
}
