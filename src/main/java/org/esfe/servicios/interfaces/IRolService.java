package org.esfe.servicios.interfaces;

import org.esfe.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IRolService {
    Page<Rol> buscarTodosPaginados(Pageable pageable);

    List<Rol> obtenerTodos();

    Optional<Rol> buscarPorId(Integer id);

    Rol crearOEditar(Rol rol);

    void eliminarPorId(Integer id);
}