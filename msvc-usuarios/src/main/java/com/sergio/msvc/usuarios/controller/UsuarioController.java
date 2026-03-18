package com.sergio.msvc.usuarios.controller;

import com.sergio.msvc.usuarios.model.entity.Usuario;
import com.sergio.msvc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<?> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        return service.buscarUsuarioPorId(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(service.guardar(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, @PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.buscarUsuarioPorId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioDb = usuarioOptional.get();
            if (usuario.getNombre()!= null) {
                usuarioDb.setNombre(usuario.getNombre());
            }
            if (usuario.getEmail()!= null) {
                usuarioDb.setEmail(usuario.getEmail());
            }
            if (usuario.getPassword()!= null) {
                usuarioDb.setPassword(usuario.getPassword());
            }
            return ResponseEntity.ok(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

}
