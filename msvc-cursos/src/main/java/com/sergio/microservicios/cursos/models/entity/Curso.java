package com.sergio.microservicios.cursos.models.entity;

import com.sergio.microservicios.cursos.models.Usuario;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cursos")
@Data
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "curso_id")
    private List<CursoUsuario> cursoUsuarios = new ArrayList<>();

    @Transient
    private List<Usuario> usuarios;

    public void addCursoUsuario(CursoUsuario cursoUsuario){
        this.cursoUsuarios.add(cursoUsuario);
    }

    public void removeCursoUsuario(CursoUsuario cursoUsuario){
        this.cursoUsuarios.remove(cursoUsuario);
    }

}
