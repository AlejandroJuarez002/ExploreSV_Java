package org.esfe.repositorios;

import org.esfe.modelos.DestinoTuristico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IDestinoTuristicoRepository extends JpaRepository<DestinoTuristico, Integer> {

    // Buscar por coincidencia parcial en el nombre (ignora mayúsculas/minúsculas)
    List<DestinoTuristico> findByNombreContainingIgnoreCase(String nombre);

    // Opcional: si también quieres buscar en descripción o ubicación
    List<DestinoTuristico> findByNombreContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrUbicacionContainingIgnoreCase(
            String nombre, String descripcion, String ubicacion
    );
}