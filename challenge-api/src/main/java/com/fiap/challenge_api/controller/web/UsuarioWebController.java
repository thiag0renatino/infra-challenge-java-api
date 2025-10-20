package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.AtualizarSenhaRequestDTO;
import com.fiap.challenge_api.dto.UsuarioDTO;
import com.fiap.challenge_api.service.AuthService;
import com.fiap.challenge_api.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/web/usuarios")
@PreAuthorize("isAuthenticated()")
public class UsuarioWebController {

    @Autowired
    private UsuarioService service;

    @Autowired
    private AuthService authService;

    @GetMapping("/me")
    public String me(Model model, Principal principal) {
        UsuarioDTO usuario = service.me(principal);
        model.addAttribute("usuario", usuario);
        return "web/usuarios/me";
    }


    // --- ALTERAR SENHA
    @GetMapping("/alterar-senha")
    public String alterarSenhaForm(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new AtualizarSenhaRequestDTO());
        }
        return "web/usuarios/alterar-senha";
    }

    // --- ALTERAR SENHA
    @PostMapping("/alterar-senha")
    public String alterarSenha(@Valid @ModelAttribute("form") AtualizarSenhaRequestDTO form,
                               BindingResult br,
                               RedirectAttributes ra,
                               Principal principal) {
        if (br.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.form", br);
            ra.addFlashAttribute("form", form);
            return "redirect:/web/usuarios/alterar-senha";
        }
        try {
            authService.changePassword(form, principal);
            ra.addFlashAttribute("msg", "Senha alterada com sucesso!");
            return "redirect:/web/usuarios/me";
        } catch (Exception ex) {
            ra.addFlashAttribute("erro", ex.getMessage());
            ra.addFlashAttribute("form", form);
            return "redirect:/web/usuarios/alterar-senha";
        }
    }
}
