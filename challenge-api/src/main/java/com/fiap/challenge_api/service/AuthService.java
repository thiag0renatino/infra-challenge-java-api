package com.fiap.challenge_api.service;

import com.fiap.challenge_api.dto.AtualizarSenhaRequestDTO;
import com.fiap.challenge_api.dto.CredencialContaDTO;
import com.fiap.challenge_api.dto.SignInDTO;
import com.fiap.challenge_api.dto.TokenDTO;
import com.fiap.challenge_api.enuns.TipoUsuario;
import com.fiap.challenge_api.model.Patio;
import com.fiap.challenge_api.model.Usuario;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.repository.UsuarioRepository;
import com.fiap.challenge_api.security.jwt.JwtTokenProvider;
import com.fiap.challenge_api.service.exception.EmailNotFoundException;
import com.fiap.challenge_api.service.exception.InvalidJwtAuthenticationException;
import com.fiap.challenge_api.service.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PatioRepository patioRepository;

    public ResponseEntity<String> register(CredencialContaDTO contaDTO) {
        String email = contaDTO.getEmail().trim().toLowerCase();
        if (repository.existsByEmailIgnoreCase(email)) {
            return new ResponseEntity<>("Email is taken!", HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(contaDTO.getSenha()));
        usuario.setNome(contaDTO.getNome());
        usuario.setStatus("ativo");
        usuario.setTipo(TipoUsuario.USER);

        Patio patio = patioRepository.findById(contaDTO.getPatio())
                .orElseThrow(() -> new ResourceNotFoundException(contaDTO.getPatio()));

        usuario.setPatio(patio);


        repository.save(usuario);
        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }


    public ResponseEntity<TokenDTO> signIn(SignInDTO signInDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInDTO.getEmail(),
                        signInDTO.getSenha()
                )
        );
        Usuario usuario = repository.findByEmailIgnoreCase(signInDTO.getEmail())
                .orElseThrow(() -> new EmailNotFoundException("Email Not found"));

        var roles = usuario.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var tokenResponse = tokenProvider.createAccessToken(
                signInDTO.getEmail(),
                roles
        );

        return ResponseEntity.ok(tokenResponse);
    }

    public ResponseEntity<TokenDTO> refreshToken(String email, String refreshToken) {
        TokenDTO tokens = tokenProvider.refreshToken(refreshToken);
        if (!tokens.getEmail().equalsIgnoreCase(email)) {
            throw new InvalidJwtAuthenticationException("Token não pertence ao email informado");
        }
        return ResponseEntity.ok(tokens);
    }

    @Transactional
    public void changePassword(AtualizarSenhaRequestDTO dto, Principal connectedUser) {
        var usuario = (Usuario) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        if (!passwordEncoder.matches(dto.getSenhaAntiga(), usuario.getSenha()))
            throw new IllegalArgumentException("Senha antiga incorreta");

        if (passwordEncoder.matches(dto.getSenhaNova(), usuario.getSenha()))
            throw new IllegalArgumentException("Nova senha não pode ser igual à atual");

        usuario.setSenha(passwordEncoder.encode(dto.getSenhaNova()));
        repository.save(usuario);
    }
}
