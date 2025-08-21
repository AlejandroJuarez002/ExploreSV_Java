package org.esfe.controladores;

import org.esfe.modelos.DestinoTuristico;
import org.esfe.modelos.ImagenDTO;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private IDestinoTuristicoService destinoTuristicoService;

    @GetMapping({"/", "/home"})
    public String index(Model model) {
            List<DestinoTuristico> destinos = destinoTuristicoService.ObtenerTodo();
            List<Map<String, Object>> destinosDTO = destinos.stream().map(dest -> {
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
            }).collect(Collectors.toList());
            model.addAttribute("destinoTuristicos", destinosDTO);
            return "home/index";
        }

    }