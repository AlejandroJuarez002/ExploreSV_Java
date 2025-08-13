package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "imagenes") // Nombre de la tabla en la base de datos
public class Imagen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "La URL de la imagen es requerida")
    private String url; // Si quieres guardar solo la ruta o nombre del archivo

    // Relación con DestinoTuristico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destino_id", nullable = false)
    private DestinoTuristico destinoTuristico;

    // Getters y Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public DestinoTuristico getDestinoTuristico() {
        return destinoTuristico;
    }

    public void setDestinoTuristico(DestinoTuristico destinoTuristico) {
        this.destinoTuristico = destinoTuristico;
    }
}

