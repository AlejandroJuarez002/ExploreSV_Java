package org.esfe.controladores;


import org.esfe.modelos.DestinoTuristico;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/destinoTuristicos")
public class DestinoTuristicoController {

    @Autowired
    private IDestinoTuristicoService destinoTuristicoService;

    @GetMapping
    public String index(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse ( 1 ) - 1;
        int pageSize = size.orElse ( 5 );
        Pageable pageable = PageRequest.of( currentPage, pageSize );

        Page<DestinoTuristico> destinoTuristicos = destinoTuristicoService.buscarTodosPaginados ( pageable );
        model.addAttribute ( "destinoTuristicos", destinoTuristicos);

        int totalPages = destinoTuristicos.getTotalPages();
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed ( 1, totalPages )
                    .boxed ()
                    .collect( Collectors.toList ( ));
            model.addAttribute ( "pageNumbers", pageNumbers );
        }

        return "destinoTuristico/index";
    }

    @GetMapping("/create")
    public String create(DestinoTuristico destinoTuristico){
        return "destinoTuristico/create";
    }

    @PostMapping("/save")
    public String save(DestinoTuristico destinoTuristico, BindingResult result, Model model, RedirectAttributes attributes){
        if(result.hasErrors()){
            model.addAttribute (destinoTuristico);
            attributes.addFlashAttribute ( "error","No se pudo guardar debido a un error." );
            return "destinoTuristico/create";
        }

        destinoTuristicoService.createOEdit ( destinoTuristico );
        attributes.addFlashAttribute ( "msg", "Grupo creado correctamente");
        return "redirect:/destinoTuristicos";
    }

    @GetMapping("/details/{Id}")
    public String details(@PathVariable("Id") Integer Id, Model model){
        DestinoTuristico destinoTuristico = destinoTuristicoService.buscarPorId (Id).get();
        model.addAttribute ( "destinoTuristico", destinoTuristico);
        return "destinoTuristico/details";
    }

    @GetMapping("/edit/{Id}")
    public String edit(@PathVariable("Id") Integer Id, Model model){
        DestinoTuristico destinoTuristico = destinoTuristicoService.buscarPorId (Id).get();
        model.addAttribute ("destinoTuristico", destinoTuristico);
        return "destinoTuristico/edit";
    }

    @GetMapping("/remove/{Id}")
    public  String remove(@PathVariable("Id") Integer Id, Model model){
        DestinoTuristico destino = destinoTuristicoService.buscarPorId(Id).get();
        model.addAttribute("destino", destino);
        return "destinoTuristico/delete";
    }

    @PostMapping("/delete")
    public String delete(DestinoTuristico destino, RedirectAttributes attributes){
        destinoTuristicoService.eliminarPorId(destino.getId());
        attributes.addFlashAttribute("msg", "Destino eliminado correctamente");
        return "redirect:/destinoTuristicos";
    }
}
