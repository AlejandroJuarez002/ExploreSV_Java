package org.esfe.controladores;


import org.esfe.modelos.DestinoTuristico;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/destinoTuristico")
public class DestinoTuristicoController {

    @Autowired
    private IDestinoTuristicoService destinoTuristicoService;

    @GetMapping
    public String index(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse ( 1 ) - 1;
        int pageSize = size.orElse ( 5 );
        Pageable pageable = PageRequest.of( currentPage, pageSize );

        Page<DestinoTuristico> destinoTuristicos = destinoTuristicoService.buscarTodosPaginados ( pageable );
        model.addAttribute ( "destinosturisticos", destinoTuristicos);

        int totalPages = destinoTuristicos.getTotalPages();
        if (totalPages > 0){
            List<Integer> pageNumbers = IntStream.rangeClosed ( 1, totalPages )
                    .boxed ()
                    .collect( Collectors.toList ( ));
            model.addAttribute ( "pageNumbers", pageNumbers );
        }

        return "destinoturistico/index";
    }

    @GetMapping("/create")
    public String create(DestinoTuristico destinoTuristico){
        return "grupo/create";
    }
}
