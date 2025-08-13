package org.esfe.controladores;

import org.esfe.modelos.Rol;
import org.esfe.servicios.interfaces.IRolService;
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
@RequestMapping("/roles")
public class RolController {
    @Autowired
    private IRolService rolService;

    /**
     * Model permitira pasar valores del controlador a la vista
     */
    @GetMapping
    private String index(Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) -1; //Si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //Tamaño de la pagina, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Rol> roles = rolService.buscarTodosPaginados(pageable);
        model.addAttribute("roles", roles);

        int totalPages = roles.getTotalPages();
        if (totalPages > 0)
        {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "rol/index";
    }

    /**
     * Accion CREATE
     */
    @GetMapping("/create")
    public String create(Rol rol){
        return "rol/create";
    }

    @PostMapping("/save")
    public String save(Rol rol, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            model.addAttribute(rol);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "rol/create";
        }

        rolService.crearOEditar(rol);
        attributes.addFlashAttribute("msg", "Rol creado correctamente.");
        return "redirect:/roles";
    }

    /**
     * Accion EDIT
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Rol rol = rolService.buscarPorId(id).get();
        model.addAttribute("rol", rol);
        return "rol/edit";
    }

    /**
     * Accion REMOVE y DELETE
     */
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Rol rol = rolService.buscarPorId(id).get();
        model.addAttribute("rol", rol);
        return "rol/delete";
    }

    @PostMapping("/delete")
    public String delete(Rol rol, RedirectAttributes attributes){
        rolService.eliminarPorId(rol.getId());
        attributes.addFlashAttribute("msg", "Rol eliminado correctamente.");
        return "redirect:/roles";
    }
}