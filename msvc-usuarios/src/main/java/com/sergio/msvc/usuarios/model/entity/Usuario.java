package com.sergio.msvc.usuarios.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @NotEmpty(message = "El nombre no puede ser vacio")
    private String nombre;

    @Column(unique=true)
    @Getter
    @Setter
    @NotEmpty(message = "El email no puede ser vacio")
    @Email(message = "El email debe ser valido")
    private String email;

    @Getter
    @Setter
    @NotEmpty(message = "La contraseña no puede ser vacia")
    private String password;

}
