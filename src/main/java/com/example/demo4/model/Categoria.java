package com.example.demo4.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Long idCategoria;

    @Column(unique = true, nullable = false, length = 50)
    private String nombre;

    private String descripcion;

    @Column(length = 10)
    private String icono;
}
