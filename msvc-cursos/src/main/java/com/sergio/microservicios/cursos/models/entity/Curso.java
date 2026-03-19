package com.sergio.microservicios.cursos.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="cursos")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @Getter
    private List<CursoUsuario> cursoUsuarios;

    @Transient
    @Getter
    @Setter
    private List<Usuario> usuarios;

    public void addCursoUsuario(CursoUsuario cursoUsuario){
        this.cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        this.cursoUsuarios.remove(cursoUsuario);
    }

}
