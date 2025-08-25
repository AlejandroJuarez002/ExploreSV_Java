package org.esfe.repositorios;

import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Page<Usuario> findByStatus(int status, Pageable pageable);

    Optional<Usuario> findByNombreUsuario(String nombreUsuario);
}