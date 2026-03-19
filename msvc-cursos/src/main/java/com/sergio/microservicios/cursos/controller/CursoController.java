package com.sergio.microservicios.cursos.controller;

import com.sergio.microservicios.cursos.models.entity.Curso;
import com.sergio.microservicios.cursos.models.Usuario;
import com.sergio.microservicios.cursos.service.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
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

    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        try {
            Optional<Usuario> usuarioOptional = service.assignCourse(usuario, cursoId);
            if (usuarioOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Error al buscar el usuario en el servicio de usuarios: " + e.getMessage()));
        }
    }

    @PostMapping("/crear-usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId) {
        try {
            Optional<Usuario> usuarioOptional = service.createUser(usuario, cursoId);
            if (usuarioOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.CREATED).body(usuarioOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Error al crear el usuario en el servicio de usuarios: " + e.getMessage()));
        }

    }

    @DeleteMapping("/eliminar-usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable Long cursoId){
        try {
            Optional<Usuario> usuarioOptional = service.deleteUser(usuario, cursoId);
            if (usuarioOptional.isPresent()) {
                return ResponseEntity.ok().body(usuarioOptional.get());
            }
            return ResponseEntity.notFound().build();
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "Error al buscar el usuario en el servicio de usuarios: " + e.getMessage()));
        }
    }

}
