package com.example.demo4.controller;

import com.example.demo4.model.Resena;
import com.example.demo4.service.ResenaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @GetMapping("/producto/{idProducto}")
    public ResponseEntity<List<Resena>> porProducto(@PathVariable Long idProducto) {
        return ResponseEntity.ok(resenaService.porProducto(idProducto));
    }

    @PostMapping
    public ResponseEntity<Resena> crear(@RequestBody Resena resena) {
        return ResponseEntity.ok(resenaService.guardar(resena));
    }
}
