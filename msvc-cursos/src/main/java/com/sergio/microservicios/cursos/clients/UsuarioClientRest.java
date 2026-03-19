package com.sergio.microservicios.cursos.clients;

import com.sergio.microservicios.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-usuarios", url = "http://localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    public Usuario findById(@PathVariable Long id);

    @GetMapping("/usuarios")
    public List<Usuario> findAllByCourse(@RequestParam Long ids);

    @PostMapping("/")
    public Usuario create(@RequestBody Usuario usuario);
}
