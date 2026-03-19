package com.sergio.microservicios.cursos.models.entity;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

public class Usuario {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nombre;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password;
}
