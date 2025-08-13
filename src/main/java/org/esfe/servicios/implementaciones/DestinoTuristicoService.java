package org.esfe.servicios.implementaciones;

import org.esfe.modelos.DestinoTuristico;
import org.esfe.repositorios.IDestinoTuristicoRepository;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Provider;
import java.util.List;
import java.util.Optional;

@Service
public class DestinoTuristicoService implements IDestinoTuristicoService {
    @Autowired
    private IDestinoTuristicoRepository destinoTuristicoRepository;

    @Override
    public Page<DestinoTuristico> buscarTodosPaginados(Pageable pageable) {
        return destinoTuristicoRepository.findAll(pageable);
    }

    @Override
    public List<DestinoTuristico> ObtenerTodo() {
        return destinoTuristicoRepository.findAll ();
    }

    @Override
    public Optional<DestinoTuristico> buscarPorId(Integer Id) {
        return destinoTuristicoRepository.findById (Id);
    }

    @Override
    public DestinoTuristico createOEdit(DestinoTuristico destinoTuristico) {
        return destinoTuristicoRepository.save (destinoTuristico);
    }

    @Override
    public void eliminarPorId(Integer Id) {
        destinoTuristicoRepository.deleteById (Id);
    }
}
