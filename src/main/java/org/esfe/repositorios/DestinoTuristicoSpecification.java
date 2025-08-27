package org.esfe.repositorios;

import org.esfe.modelos.DestinoTuristico;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class DestinoTuristicoSpecification {
    public static Specification<DestinoTuristico> tieneNombre(String nombre) {
        return (root, query, cb) ->
                nombre.isEmpty() ? cb.conjunction() : cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%");
    }

    public static Specification<DestinoTuristico> tieneCategoria(String categoria) {
        return (root, query, cb) -> {
            if (categoria.isEmpty()) {
                return cb.conjunction();
            }
            Join<DestinoTuristico, org.esfe.modelos.Categoria> categoriaJoin = root.join("categoria", JoinType.LEFT);
            return cb.like(cb.lower(categoriaJoin.get("nombre")), "%" + categoria.toLowerCase() + "%");
        };
    }

    public static Specification<DestinoTuristico> tieneDepartamento(String departamento) {
        return (root, query, cb) -> {
            if (departamento.isEmpty()) {
                return cb.conjunction();
            }
            Join<DestinoTuristico, org.esfe.modelos.Departamento> departamentoJoin = root.join("departamento", JoinType.LEFT);
            return cb.like(cb.lower(departamentoJoin.get("nombre")), "%" + departamento.toLowerCase() + "%");
        };
    }
}

