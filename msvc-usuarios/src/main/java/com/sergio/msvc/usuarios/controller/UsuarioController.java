package com.sergio.msvc.usuarios.controller;

import com.sergio.msvc.usuarios.model.entity.Usuario;
import com.sergio.msvc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<?> search() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/byIds")
    public ResponseEntity<?> getUsersByIds(@RequestParam(name = "ids") List<Long> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        return service.findUserById(id)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byEmail/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        return service.findUserByEmail(email)
                .map(usuario -> ResponseEntity.ok().body(usuario))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> createUser(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }
        if (service.findUserByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con ese email"));
        }
        return ResponseEntity.ok(service.saveUser(usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        service.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@Valid @RequestBody Usuario usuario, @PathVariable Long id, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }

        if (service.findUserByEmail(usuario.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Ya existe un usuario con ese email"));
        }

        Optional<Usuario> usuarioOptional = service.findUserById(id);
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
            return ResponseEntity.ok(service.saveUser(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<?> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(err -> {
            errores.put(err.getField(), "El campo " + err.getField() + " " + err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
