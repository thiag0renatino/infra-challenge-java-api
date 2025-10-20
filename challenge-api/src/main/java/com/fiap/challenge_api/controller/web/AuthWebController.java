package com.fiap.challenge_api.controller.web;

import com.fiap.challenge_api.dto.CredencialContaDTO;
import com.fiap.challenge_api.dto.SignInDTO;
import com.fiap.challenge_api.enuns.TipoUsuario;
import com.fiap.challenge_api.model.Patio;
import com.fiap.challenge_api.model.Usuario;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.repository.UsuarioRepository;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web")
public class AuthWebController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PatioRepository patioRepository;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("form", new SignInDTO());
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("form", new CredencialContaDTO());
        model.addAttribute("patios", patioRepository.findAll());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@ModelAttribute("form") CredencialContaDTO form,
                             @RequestParam("confirmacaoSenha") String confirmacaoSenha,
                             RedirectAttributes ra,
                             Model model) {

        if (!form.getSenha().equals(confirmacaoSenha)) {
            model.addAttribute("erro", "Senha e confirmação não coincidem.");
            return "register";
        }
        if (usuarioRepository.existsByEmailIgnoreCase(form.getEmail())) {
            model.addAttribute("erro", "Email já cadastrado.");
            return "register";
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(form.getEmail());
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        usuario.setNome(form.getNome());
        usuario.setStatus("ativo");
        usuario.setTipo(TipoUsuario.USER);

        Patio patio = patioRepository.findById(form.getPatio())
                .orElseThrow(() -> new ResourceNotFoundException(form.getPatio()));

        usuario.setPatio(patio);
        usuarioRepository.save(usuario);

        ra.addAttribute("registered", "true");
        return "redirect:/web/login";
    }

    @GetMapping({"", "/"})
    public String home() {
        return "home";
    }
}
