package org.esfe.controladores;

import org.esfe.modelos.Departamento;
import org.esfe.servicios.interfaces.IDepartamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {
    @Autowired
    private IDepartamentoService departamentoService;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; //si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //tamano de la pagina se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Departamento> departamentos = departamentoService.buscarTodosPaginados(pageable);
        model.addAttribute("departamentos", departamentos);

        int totalPages = departamentos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "departamento/index";
    }

    @GetMapping("/create")
    public String create(Departamento departamento){
        return "departamento/create";
    }

    @PostMapping("/save")
    public String save(Departamento departamento, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(departamento);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error");
            return "departamento/create";
        }

        departamentoService.crearOEditar(departamento);
        attributes.addFlashAttribute("msg", "Grupo creado correctamente");
        return "redirect:/departamentos";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Departamento departamento = departamentoService.buscarPorId(id).get();
        model.addAttribute("departamento", departamento);
        return "departamento/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Departamento departamento = departamentoService.buscarPorId(id).get();
        model.addAttribute("departamento", departamento);
        return "departamento/edit";
    }


    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Departamento departamento = departamentoService.buscarPorId(id).get();
        model.addAttribute("departamento", departamento);
        return "departamento/delete";
    }

    @PostMapping("/delete")
    public String delete(Departamento departamento, RedirectAttributes attributes) {
        departamentoService.eliminarPorId(departamento.getId());
        attributes.addFlashAttribute("msg", "Departamento eliminado correctamente");
        return "redirect:/departamentos";
    }
}
