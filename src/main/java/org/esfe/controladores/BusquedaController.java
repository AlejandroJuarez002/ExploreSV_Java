package org.esfe.controladores;

import org.esfe.modelos.Categoria;
import org.esfe.modelos.Departamento;
import org.esfe.modelos.DestinoTuristico;
import org.esfe.repositorios.ICategoriaRepository;
import org.esfe.repositorios.IDepartamentoRepository;
import org.esfe.repositorios.ISDestinoTuristicoRepository;
import org.esfe.repositorios.DestinoTuristicoSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class BusquedaController {

    @Autowired
    private ISDestinoTuristicoRepository destinoRepo;

    @Autowired
    private ICategoriaRepository categoriaRepo;

    @Autowired
    private IDepartamentoRepository departamentoRepo;

    @GetMapping("/busqueda")
    public String busqueda(
            @RequestParam(name = "nombre", required = false, defaultValue = "") String nombre,
            @RequestParam(name = "categoria", required = false, defaultValue = "") String categoria,
            @RequestParam(name = "departamento", required = false, defaultValue = "") String departamento,
            Model model) {

        // Obtener listas de categorías y departamentos para los combo box
        List<Categoria> categorias = categoriaRepo.findAll();
        List<Departamento> departamentos = departamentoRepo.findAll();

        // Lógica de búsqueda con Specification
        Specification<DestinoTuristico> spec =
                DestinoTuristicoSpecification.tieneNombre(nombre)
                        .and(DestinoTuristicoSpecification.tieneCategoria(categoria))
                        .and(DestinoTuristicoSpecification.tieneDepartamento(departamento));

        List<DestinoTuristico> resultados = destinoRepo.findAll(spec);

        // Atributos para la vista
        model.addAttribute("destinos", resultados);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        model.addAttribute("departamento", departamento);
        model.addAttribute("categorias", categorias);
        model.addAttribute("departamentos", departamentos);

        return "busqueda/index";
    }
}
