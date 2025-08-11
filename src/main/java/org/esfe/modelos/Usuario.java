package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.LinkedList;
import java.util.List;

@Entity /*Evitar error q no se queda ejecutando*/
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Su nombre es requerido")
    private String nombre;

    @NotBlank(message = "Su apellido es requerido")
    private String apellido;

    @NotBlank(message = "El nombre de usuario de requerido")
    private String nombreUsuario;

    @NotBlank(message = "La contraseña es requerida")
    private String clave;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="usuario_rol",
            joinColumns = @JoinColumn(name="usuario_id"),
            inverseJoinColumns = @JoinColumn(name="rol_id"))
    private List<Rol> roles;

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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public void setRoles(List<Rol> roles) {
        this.roles = roles;
    }

    /**
     * Metodo agregar roles, por si no hay en la tabla
     */
    public void agregar(Rol tempRol){
        if (roles == null){
            roles = new LinkedList<>();
        }
        roles.add(tempRol);
    }
}