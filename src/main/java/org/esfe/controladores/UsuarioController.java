package org.esfe.controladores;

import org.esfe.modelos.Rol;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IRolService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
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
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private IUsuarioService usuarioService;

    @Autowired
    private IRolService rolService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    private String index(Model model,
                         @RequestParam("page") Optional<Integer> page,
                         @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) -1; //Si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //Tamaño de la pagina, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuarios = usuarioService.buscarTodosPaginados(pageable);
        model.addAttribute("usuarios", usuarios);

        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0)
        {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "usuario/index";
    }

    /**
     * Accion CREATE
     */
    @GetMapping("/create")
    public String create(Usuario usuario){
        return "usuario/create";
    }

    @PostMapping("/save")
    public String save(Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes){

        if (result.hasErrors()){
            model.addAttribute(usuario);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "usuario/create";
        }

        /**
         * Validar que el rol exista si se asignó uno
         */
        if (usuario.getRol() == null) {
            Rol rol = rolService.buscarPorId(1)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            usuario.setRol(rol);
        } else {
            attributes.addFlashAttribute("error", "Debe seleccionar un rol válido.");
            return "usuario/create";
        }

        String password = passwordEncoder.encode(usuario.getClave());

        usuario.setStatus(1);
        usuario.setClave(password);
        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario creado correctamente.");
        return "redirect:/usuarios";
    }

    /**
     * Accion DETAILS
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/details";
    }

    /**
     * Accion EDIT
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/edit";
    }

    /**
     * Accion REMOVE y DELETE
     */
    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model){
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/delete";
    }

    @PostMapping("/delete")
    public String delete(Usuario usuario, RedirectAttributes attributes){
        usuarioService.eliminarPorId(usuario.getId());
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }
    //@PutMapping para desactivar usuario y pasar a inactivo y en RolService poner metodo para modificar
}