package org.esfe.controladores;


import org.esfe.modelos.DestinoTuristico;
import org.esfe.modelos.Imagen;
import org.esfe.modelos.ImagenDTO;
import org.esfe.repositorios.IDestinoTuristicoRepository;
import org.esfe.servicios.interfaces.IDestinoTuristicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/destinoTuristicos")
public class DestinoTuristicoController {

    @Autowired
    private IDestinoTuristicoService destinoTuristicoService;

    @Autowired
    private IDestinoTuristicoRepository destinoTuristicoRepository;

    @GetMapping
    public String index(Model model,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam(value = "q", required = false) String q) {

        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(10);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<DestinoTuristico> destinos;

        if (q != null && !q.isEmpty()) {
            // Si hay búsqueda, no paginar (opcional: se puede implementar paginación también)
            List<DestinoTuristico> resultados = destinoTuristicoRepository.findByNombreContainingIgnoreCase(q);
            destinos = new PageImpl<> (resultados, pageable, resultados.size());
        } else {
            destinos = destinoTuristicoService.buscarTodosPaginados(pageable);
        }

        // Convertir imágenes a Base64
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
        model.addAttribute("q", q); // Mantener valor de búsqueda

        int totalPages = destinos.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "destinoTuristico/index";
    }

    @GetMapping("/create")
    public String create(DestinoTuristico destinoTuristico) {
        return "destinoTuristico/create";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute DestinoTuristico destinoTuristico,
                       @RequestParam("imagenFiles") List<MultipartFile> imagenFiles,
                       BindingResult result,
                       Model model,
                       RedirectAttributes attributes) {

        if (result.hasErrors()) {
            model.addAttribute(destinoTuristico);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "destinoTuristico/create";
        }

        try {
            List<Imagen> listaImagenes = new ArrayList<>();
            for (MultipartFile file : imagenFiles) {
                if (!file.isEmpty()) {
                    Imagen img = new Imagen();
                    img.setBytesArrayImage(file.getBytes());
                    img.setDestinoTuristico(destinoTuristico);
                    listaImagenes.add(img);
                }
            }
            destinoTuristico.setImagenes(listaImagenes);

        } catch (IOException e) {
            attributes.addFlashAttribute("error", "Error al procesar las imágenes.");
            return "destinoTuristico/create";
        }

        destinoTuristicoService.createOEdit(destinoTuristico);
        attributes.addFlashAttribute("msg", "Destino turístico creado correctamente.");
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

    @GetMapping("/search")
    public String search(@RequestParam("q") String query, Model model) {
        List<DestinoTuristico> resultados = destinoTuristicoRepository.findByNombreContainingIgnoreCase(query);
        model.addAttribute("destinos", resultados);
        model.addAttribute("q", query);
        return "destinoTuristicos/index"; // usa la misma vista que el listado normal
    }

}
