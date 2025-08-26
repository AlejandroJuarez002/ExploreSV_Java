package org.esfe.controladores;

import org.esfe.modelos.DestinoTuristico;
import org.esfe.modelos.ImagenDTO;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class HomeController {


    @Autowired
    private IDestinoTuristicoService destinoTuristicoService;

    @GetMapping({"/", "/home"})
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size) {

        int currentPage = page.orElse(1) - 1;  // Spring maneja 0-based index
        int pageSize = size.orElse(6);         // cantidad de destinos por página

        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<DestinoTuristico> destinos = destinoTuristicoService.buscarTodosPaginados(pageable);

        // Convertir a DTO con imágenes base64
        Page<Map<String, Object>> destinosDTO = destinos.map(dest -> {
            List<ImagenDTO> imagenesDTO = dest.getImagenes().stream()
                    .map(img -> new ImagenDTO(
                            img.getId(),
                            img.getBytesArrayImage() != null ?
                                    Base64.getEncoder().encodeToString(img.getBytesArrayImage()) : null))
                    .collect(Collectors.toList());

            Map<String, Object> dto = new HashMap<>();
            dto.put("destino", dest);
            dto.put("imagenes", imagenesDTO);
            return dto;
        });

        model.addAttribute("destinoTuristicos", destinosDTO);

        // Generar lista de páginas
        int totalPages = destinos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "home/index";
    }
}