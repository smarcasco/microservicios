package com.sergio.microservicios.cursos.models;

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
