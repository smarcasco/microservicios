package com.sergio.msvc.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique=true)
    private String email;

}
