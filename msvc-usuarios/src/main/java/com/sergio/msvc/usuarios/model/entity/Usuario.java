package com.sergio.msvc.usuarios.model.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique=true)
    @Getter
    private String email;

}
