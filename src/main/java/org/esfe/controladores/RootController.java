package org.esfe.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {
    @GetMapping("/")
    public String redirectRoot(){
        return "redirect:/home";
    }
}