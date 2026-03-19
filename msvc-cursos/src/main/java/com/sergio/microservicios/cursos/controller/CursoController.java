package com.sergio.microservicios.cursos.controller;

import com.sergio.microservicios.cursos.models.entity.Curso;
import com.sergio.microservicios.cursos.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class CursoController {

    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.search());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return service.findCourseById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@RequestBody Curso usuario) {
        return ResponseEntity.ok(service.save(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@RequestBody Curso usuario, @PathVariable Long id) {
        Optional<Curso> usuarioOptional = service.findCourseById(id);
        if (usuarioOptional.isPresent()) {
            Curso usuarioDb = usuarioOptional.get();
            if (usuario.getNombre()!= null) {
                usuarioDb.setNombre(usuario.getNombre());
            }
            return ResponseEntity.ok(service.save(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

}
