package com.sergio.microservicios.cursos.models.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cursos_usuarios")
@EqualsAndHashCode
public class CursoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Column(name = "usuario_id",unique = true)
    @Getter
    @Setter
    private Long usuarioId;

}
