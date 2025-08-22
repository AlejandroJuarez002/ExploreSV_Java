package org.esfe.modelos;


import groovy.lang.GString;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "destinoTuristicos")
public class DestinoTuristico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank (message = "La descripcion es requerida")
    private String descripcion;

    @NotBlank (message = "La ubicacion es requerida")
    private String ubicacion;

    @NotBlank (message = "El horario es requerido")
    private String horario;

    // Relación con Imagen (foránea en Imagen)
    @OneToMany(mappedBy = "destinoTuristico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Imagen> imagenes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public List<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<Imagen> imagenes) {
        this.imagenes = imagenes;
    }
}
