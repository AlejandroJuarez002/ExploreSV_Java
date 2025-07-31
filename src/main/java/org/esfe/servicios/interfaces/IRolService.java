package org.esfe.servicios.interfaces;

import org.esfe.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRolService {
    Page<Rol> buscarTodosPaginados(Pageable pageable);

    List<Rol> obtenerTodos();

    Rol crearOEditar(Rol rol);

    void eliminarPorId(Integer id);
}