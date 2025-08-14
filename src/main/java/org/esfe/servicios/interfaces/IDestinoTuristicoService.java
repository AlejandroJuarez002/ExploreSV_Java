package org.esfe.servicios.interfaces;


import org.esfe.modelos.DestinoTuristico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IDestinoTuristicoService {

    Page<DestinoTuristico> buscarTodosPaginados(Pageable pageable);

    List<DestinoTuristico> ObtenerTodo();

    Optional<DestinoTuristico> buscarPorId(Integer Id);

    DestinoTuristico createOEdit (DestinoTuristico destinoTuristico);

    void eliminarPorId(Integer destinoTuristico);
}
