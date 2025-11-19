package org.esfe.modelos;


import groovy.lang.GString;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "destinoTuristicos")
public class DestinoTuristico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    @Size(max = 100, message = "La nombre no puede tener más de 100 caracteres")
    private String nombre;

    @NotBlank (message = "La descripcion es requerida")
    @Size(max = 2500, message = "La descripción no puede tener más de 5000 caracteres")
    private String descripcion;

    @NotBlank (message = "La ubicacion es requerida")
    @Size(max = 300, message = "La ubicación no puede tener más de 300 caracteres")
    private String ubicacion;

    @NotBlank (message = "El horario es requerido")
    @Size(max = 100, message = "El horario no puede tener más de 100 caracteres")
    private String horario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    // Relación con Imagen (foránea en Imagen)
    @OneToMany(
            mappedBy = "destinoTuristico",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER) //Permite q las imagenes se muestren en cualquier ejecucion de usuarios
    private List<Imagen> imagenes= new ArrayList<>();

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

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}