package com.sergio.msvc.usuarios.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="msvc-cursos", url="http://localhost:8002")
public interface CursoClientRest {

    @DeleteMapping("/eliminar-usuario/{id}")
    public void eliminarUsuario(@PathVariable Long id);

}
