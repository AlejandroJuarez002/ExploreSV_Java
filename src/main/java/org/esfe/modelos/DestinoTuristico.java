package org.esfe.modelos;


import groovy.lang.GString;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "destinosturisticos")
public class DestinoTuristico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank (message = "La descripcion es requerida")
    private String descripcion;

    @NotBlank (message = "La ubicacion es requerida")
    private String ubicacion;

    @NotBlank (message = "El horario es requerido")
    private String horario;

    // Relación con Imagen (foránea en Imagen)
    //@OneToMany(mappedBy = "destinoTuristico", cascade = CascadeType.ALL, orphanRemoval = true)
    //private List<Imagen> imagenes

}
