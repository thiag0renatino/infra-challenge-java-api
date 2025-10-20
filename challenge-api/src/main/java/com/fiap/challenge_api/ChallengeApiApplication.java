package com.fiap.challenge_api;

import com.fiap.challenge_api.enuns.TipoUsuario;
import com.fiap.challenge_api.model.Patio;
import com.fiap.challenge_api.model.Usuario;
import com.fiap.challenge_api.repository.PatioRepository;
import com.fiap.challenge_api.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(
        info = @Info(
                title = "API Challenge 2025",
                version = "1.0",
                description = "Documentação da API Rest - Challenge Mottu"
        )
)
public class ChallengeApiApplication {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(ChallengeApiApplication.class, args);
    }

//    @Override
//    public void run(String... args) {
//        final String emailAdmin = "admin@mottu.com";
//
//        if (!usuarioRepository.existsByEmailIgnoreCase(emailAdmin)) {
//            Patio patio = patioRepository.findById(1L).orElse(null);
//
//            Usuario admin = new Usuario();
//            admin.setNome("Administrador Mottu");
//            admin.setEmail(emailAdmin);
//            admin.setSenha(passwordEncoder.encode("admin123"));
//            admin.setStatus("ativo");
//            admin.setTipo(TipoUsuario.ADMIN);
//            admin.setPatio(patio);
//
//            usuarioRepository.save(admin);
//
//            System.out.println("\n=== Usuário ADMIN criado com sucesso ===");
//            System.out.println("Email: " + emailAdmin);
//            System.out.println("Senha: admin123");
//            System.out.println("========================================\n");
//        } else {
//            System.out.println("\nUsuário ADMIN já existente\n");
//            System.out.println("Email: " + emailAdmin);
//            System.out.println("Senha: admin123");
//        }
//    }
}
