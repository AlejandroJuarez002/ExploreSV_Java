package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Categoria;
import org.esfe.repositorios.ICategoriaRepository;
import org.esfe.servicios.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Se implementan los metodos de ICategoriaService
 */
@Service
public class CategoriaService implements ICategoriaService {
    @Autowired
    private ICategoriaRepository categoriaRepository;

    /**
     * Devuelve registros de manera paginada
     * @param pageable
     * @return
     */
    @Override
    public Page<Categoria> buscarTodosPaginados(Pageable pageable) {
        return categoriaRepository.findAll(pageable);
    }

    /**
     * Devuelve una lista
     * @return
     */
    @Override
    public List<Categoria> obtenerTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    /**
     * Devuelve metodo save
     * @param categoria
     * @return
     */
    @Override
    public Categoria crearOEditar(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaRepository.deleteById(id);
    }
}