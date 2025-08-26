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
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Usuario> usuarios = usuarioService.buscarPorStatus(1, pageable);
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

    @GetMapping("/create")
    public String create(Usuario usuario) {
        return "usuario/create";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes attributes,
            @RequestParam(required = false) String cambiarClave
    ) {
        if (result.hasErrors()) {
            model.addAttribute(usuario);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return usuario.getId() == null ? "usuario/create" : "usuario/edit";
        }

        if (usuario.getRol() == null) {
            Rol rol = rolService.buscarPorId(1)
                    .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
            usuario.setRol(rol);
        }

        // Si el checkbox está marcado, validar que la contraseña no esté vacía y sea diferente a la anterior
        if (cambiarClave != null && !cambiarClave.isEmpty()) {
            if (usuario.getClave() == null || usuario.getClave().trim().isEmpty()) {
                attributes.addFlashAttribute("error", "Debe ingresar una nueva contraseña.");
                return "redirect:/usuarios/edit/" + usuario.getId();
            }
            Usuario usuarioExistente = usuarioService.buscarPorId(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            if (passwordEncoder.matches(usuario.getClave(), usuarioExistente.getClave())) {
                attributes.addFlashAttribute("error", "La nueva contraseña debe ser diferente a la anterior.");
                return "redirect:/usuarios/edit/" + usuario.getId();
            }
            String password = passwordEncoder.encode(usuario.getClave());
            usuario.setClave(password);
        } else if (usuario.getId() != null) {
            // Si no se cambia la contraseña, mantener la existente
            Usuario usuarioExistente = usuarioService.buscarPorId(usuario.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            usuario.setClave(usuarioExistente.getClave());
        } else if (usuario.getId() == null && (usuario.getClave() == null || usuario.getClave().trim().isEmpty())) {
            // Si es un nuevo usuario, la contraseña es obligatoria
            attributes.addFlashAttribute("error", "La contraseña es obligatoria para nuevos usuarios.");
            return "usuario/create";
        } else if (usuario.getId() == null) {
            // Si es un nuevo usuario, encriptar la contraseña
            String password = passwordEncoder.encode(usuario.getClave());
            usuario.setClave(password);
        }

        usuario.setStatus(1);
        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", usuario.getId() == null ? "Usuario creado correctamente." : "Usuario editado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id).get();
        model.addAttribute("usuario", usuario);
        return "usuario/edit";
    }

    @GetMapping("/desactivate/{id}")
    public String showDesactivateView(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuarioLogeado = usuarioService.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario logeado no encontrado"));
        boolean esMismoUsuario = usuarioLogeado.getId().equals(usuario.getId());
        model.addAttribute("usuario", usuario);
        model.addAttribute("esMismoUsuario", esMismoUsuario);
        return "usuario/desactivate";
    }

    @PostMapping("/desactivate/{id}")
    public String desactivate(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario usuarioLogeado = usuarioService.buscarPorNombreUsuario(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario logeado no encontrado"));
        if (usuarioLogeado.getId().equals(id)) {
            attributes.addFlashAttribute("error", "No puedes desactivar tu propio usuario.");
            return "redirect:/usuarios";
        }
        Usuario usuarioADesactivar = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuarioADesactivar.setStatus(0);
        usuarioService.crearOEditar(usuarioADesactivar);
        attributes.addFlashAttribute("msg", "Usuario desactivado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/inactive")
    public String inactiveUsers(Model model,
                                @RequestParam("page") Optional<Integer> page,
                                @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1) - 1;
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage, pageSize);
        Page<Usuario> usuarios = usuarioService.buscarPorStatus(0, pageable);
        model.addAttribute("usuarios", usuarios);
        int totalPages = usuarios.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "usuario/inactive_index";
    }

    @GetMapping("/activate/{id}")
    public String showActivateView(@PathVariable("id") Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "usuario/activate";
    }

    @PostMapping("/activate/{id}")
    public String activate(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        Usuario usuario = usuarioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setStatus(1);
        usuarioService.crearOEditar(usuario);
        attributes.addFlashAttribute("msg", "Usuario reactivado correctamente.");
        return "redirect:/usuarios/inactive";
    }
}