package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Estado;
import org.esfe.repositorios.IEstadoRepository;
import org.esfe.servicios.interfaces.IEstadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoService implements IEstadoService {
    @Autowired
    private IEstadoRepository estadoRepository;

    @Override
    public Page<Estado> buscarTodoPaginados(Pageable pageable) {
        return estadoRepository.findAll(pageable);
    }

    @Override
    public List<Estado> obtenerTodos() {
        return estadoRepository.findAll();
    }

    @Override
    public Optional<Estado> buscarPorId(Integer id) {
        return estadoRepository.findById(id);
    }

    @Override
    public Estado crearOEditar(Estado estado) {
        return estadoRepository.save(estado);
    }
}