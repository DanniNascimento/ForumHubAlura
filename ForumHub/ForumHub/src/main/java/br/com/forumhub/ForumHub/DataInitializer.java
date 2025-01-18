package br.com.forumhub.ForumHub;

import br.com.forumhub.ForumHub.model.entities.Usuario;
import br.com.forumhub.ForumHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            if (usuarioRepository.findByEmail("dnrcriartes@gmail.com").isEmpty()) {
                Usuario admin = new Usuario("Danni", "dnrcriartes@gmail.com", passwordEncoder.encode("1234567890"));
                usuarioRepository.save(admin);
            }
        };
    }
}