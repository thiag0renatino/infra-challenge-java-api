package com.fiap.challenge_api.controller;

import com.fiap.challenge_api.dto.CredencialContaDTO;
import com.fiap.challenge_api.dto.SignInDTO;
import com.fiap.challenge_api.dto.TokenDTO;
import com.fiap.challenge_api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @Operation(summary = "Registra novo usuário na aplicação",
            description = "Cria um novo usuário capz de receber autenticação para utilizaçõ dos endpoints")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CredencialContaDTO contaDTO) {
        return service.register(contaDTO);
    }

    @Operation(summary = "Autentica o usuário e emite tokens JWT (access e refresh)",
            description = "Valida as credenciais (email e senha) enviadas no corpo da requisição.")
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInDTO signInDTO) {
        if (credentialsIsInvalid(signInDTO))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");

        var token = service.signIn(signInDTO);
        if (token == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid client request");

        return ResponseEntity.ok().body(token);
    }

    private static boolean credentialsIsInvalid(SignInDTO signInDTO) {
        return signInDTO == null ||
                StringUtils.isBlank(signInDTO.getEmail()) ||
                StringUtils.isBlank(signInDTO.getSenha());
    }

    @Operation(summary = "Refresh Token para usuário autenticado")
    @PutMapping("/refresh/{email}")
    public ResponseEntity<TokenDTO> refreshToken(@PathVariable("email") String email, @RequestHeader("Authorization") String refreshToken) {
        if (parameterAreInvalid(email, refreshToken)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build();

        return service.refreshToken(email, refreshToken);
    }

    private boolean parameterAreInvalid(String email, String refreshToken) {
        return StringUtils.isBlank(email) || StringUtils.isBlank(refreshToken);
    }
}
