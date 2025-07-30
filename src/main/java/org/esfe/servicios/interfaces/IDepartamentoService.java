package org.esfe.servicios.interfaces;

import org.esfe.modelos.Departamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDepartamentoService {
    Page<Departamento> buscarTodosPaginados(Pageable pageable);

    List<Departamento> obtenerTodos();

    Optional<Departamento> buscarPorId(Integer id);

    Departamento crearOEditar(Departamento departamento);

    void eliminarPorId(Integer id);
}