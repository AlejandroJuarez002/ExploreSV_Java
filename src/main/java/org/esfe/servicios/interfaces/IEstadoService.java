package org.esfe.servicios.interfaces;

import org.esfe.modelos.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEstadoService {
    Page<Estado> buscarTodoPaginados(Pageable pageable);

    List<Estado> obtenerTodos();

    Optional<Estado> buscarPorId(Integer id);

    Estado crearOEditar(Estado estado);

    //Void eliminarPorId(Integer id);
}