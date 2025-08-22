package org.esfe.controladores;

import org.esfe.modelos.Rol;
import org.esfe.modelos.Usuario;
import org.esfe.servicios.interfaces.IRolService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                         @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1; //Si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //Tamaño de la pagina, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuarios = usuarioService.buscarPorStatus(1, pageable); //Solo usuarios activos
        model.addAttribute("usuarios", usuarios);

        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0) {
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
    public String create(Usuario usuario) {
        return "usuario/create";
    }

    @PostMapping("/save")
    public String save(Usuario usuario, BindingResult result, Model model, RedirectAttributes attributes) {

        if (result.hasErrors()) {
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
    public String details(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/details";
    }

    /**
     * Accion EDIT
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/edit";
    }

    /**
     * Accion DESACTIVATE
     */
    @GetMapping("/desactivate/{id}")
    public String showDesactivateView(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Usuario logeado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Usuario usuarioLogeado = usuarioService.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario logeado no encontrado"));

        // Se agrega bandera si es el mismo
        boolean esMismoUsuario = usuarioLogeado.getId().equals(usuario.getId());

        model.addAttribute("usuario", usuario);
        model.addAttribute("esMismoUsuario", esMismoUsuario);

        return "usuario/desactivate";
    }

    @PostMapping("/desactivate/{id}")
    public String desactivate(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        // Obtiene el usuario autenticado desde Spring Security
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Busca en base de datos el usuario logeado
        Usuario usuarioLogeado = usuarioService.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario logeado no encontrado"));

        // Valida que no pueda desactivar el usuario logeado
        if (usuarioLogeado.getId().equals(id)) {
            attributes.addFlashAttribute("error", "No puedes desactivar tu propio usuario.");
            return "redirect:/usuarios";
        }

        // Desactivación de otro usuario
        Usuario usuarioADesactivar = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuarioADesactivar.setStatus(0); // Se pasa a inactivo
        usuarioService.crearOEditar(usuarioADesactivar); // Se guarda

        attributes.addFlashAttribute("msg", "Usuario desactivado correctamente.");
        return "redirect:/usuarios"; // Redirige al listado de usuarios activos
    }

    /**
     * Accion REACTIVE USERS
     */
    @GetMapping("/inactive")
    public String inactiveUsers(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<Usuario> usuarios = usuarioService.buscarPorStatus(0, pageable); // status 0 = inactivos
        model.addAttribute("usuarios", usuarios);

        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "usuario/inactive_index"; // la vista para listar usuarios inactivos
    }

    @GetMapping("/activate/{id}")
    public String showActivateView(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuario/activate"; // vista de confirmación de reactivación
    }

    @PostMapping("/activate/{id}")
    public String activate(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setStatus(1); // Se pasa a activo
        usuarioService.crearOEditar(usuario); // Se guarda

        attributes.addFlashAttribute("msg", "Usuario reactivado correctamente.");
        return "redirect:/usuarios/inactive"; // Vuelve al listado de inactivos
    }
}