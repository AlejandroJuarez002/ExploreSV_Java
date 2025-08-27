package org.esfe.repositorios;

import org.esfe.modelos.DestinoTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ISDestinoTuristicoRepository extends JpaRepository<DestinoTuristico, Long>, JpaSpecificationExecutor<DestinoTuristico> {

    // Buscar por Nombre, Nombre de Categoría y Nombre de Departamento (ignorando mayúsculas/minúsculas)
    List<DestinoTuristico> findByNombreContainingIgnoreCaseAndCategoria_NombreContainingIgnoreCaseAndDepartamento_NombreContainingIgnoreCase(
            String nombre,
            String categoriaNombre,
            String departamentoNombre
    );
}
