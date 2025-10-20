package com.fiap.challenge_api.service;

import com.fiap.challenge_api.controller.UsuarioController;
import com.fiap.challenge_api.dto.UsuarioDTO;
import com.fiap.challenge_api.mapper.UsuarioMapper;
import com.fiap.challenge_api.model.Patio;
import com.fiap.challenge_api.model.Usuario;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.repository.UsuarioRepository;
import com.fiap.challenge_api.service.exception.EmailNotFoundException;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    PatioRepository patioRepository;

    @Autowired
    private UsuarioMapper mapper;

    public List<UsuarioDTO> findAll() {
        List<UsuarioDTO> dtos = repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();

        dtos.forEach(UsuarioService::addHateoasLinks);
        return dtos;
    }

    public UsuarioDTO findById(Long id) {
        UsuarioDTO dto = repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        addHateoasLinks(dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public UsuarioDTO me(Principal principal) {
        return findByEmail(principal.getName());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));
    }

    public UsuarioDTO findByEmail(String email) {
        Usuario usuario = repository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new EmailNotFoundException(email));
        UsuarioDTO dto = mapper.toDTO(usuario);
        addHateoasLinks(dto);
        return dto;
    }

    public UsuarioDTO update(Long id, UsuarioDTO dto) {
        Usuario usuarioExist = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));

        usuarioExist.setNome(dto.getNome());
        usuarioExist.setEmail(dto.getEmail());
        usuarioExist.setStatus(dto.getStatus());

        if (dto.getPatioId() != null) {
            Patio patio = new Patio();
            patio.setIdPatio(dto.getPatioId());
            usuarioExist.setPatio(patio);
        }

        Usuario usuarioAtt = repository.save(usuarioExist);
        UsuarioDTO resp = mapper.toDTO(usuarioAtt);
        addHateoasLinks(resp);
        return resp;
    }

    public void delete(Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        repository.delete(usuario);
    }

    static void addHateoasLinks(UsuarioDTO dto) {
        boolean isAuthenticated = isAuthenticated();
        if (hasRole()) {
            dto.add(linkTo(methodOn(UsuarioController.class).findAll()).withRel("findAll").withType("GET"));
        }
        if (dto.getIdUsuario() != null && hasRole()) {
            dto.add(linkTo(methodOn(UsuarioController.class).findById(dto.getIdUsuario())).withSelfRel().withType("GET"));
            dto.add(linkTo(methodOn(UsuarioController.class).update(dto.getIdUsuario(), new UsuarioDTO())).withRel("update").withType("PUT"));
            dto.add(linkTo(methodOn(UsuarioController.class).delete(dto.getIdUsuario())).withRel("delete").withType("DELETE"));
        }
        if (isAuthenticated && dto.getEmail() != null && !dto.getEmail().isBlank()) {
            dto.add(linkTo(methodOn(UsuarioController.class).findByEmail(dto.getEmail())).withRel("findByEmail").withType("GET"));
        }
        if (isAuthenticated) {
            dto.add(linkTo(methodOn(UsuarioController.class).atualizarSenha(null, null)).withRel("atualizarSenha").withType("PATCH"));
        }
    }

    private static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated();
    }

    private static boolean hasRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return false;
        String adminRole = "ROLE_" + "ADMIN";
        return auth.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(adminRole));
    }
}
