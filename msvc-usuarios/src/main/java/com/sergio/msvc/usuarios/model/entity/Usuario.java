package com.sergio.msvc.usuarios.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "no puede ser vacio")
    private String nombre;

    @Column(unique=true)
    @Getter
    @Setter
    @NotBlank(message = "no puede ser vacio")
    @Email(message = "debe ser valido")
    private String email;

    @Getter
    @Setter
    @NotBlank(message = "no puede ser vacio")
    private String password;

}
