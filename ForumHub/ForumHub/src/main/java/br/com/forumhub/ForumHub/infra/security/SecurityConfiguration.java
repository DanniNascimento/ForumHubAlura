package br.com.forumhub.ForumHub.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Classe de configuração de segurança da aplicação.
 * Define filtros, políticas de autenticação e codificação de senhas.
 */
public class SecurityConfiguration {

    // Filtro de segurança personalizado, injetado automaticamente pelo Spring.
    @Autowired
    private SecurityFilter securityFilter;

    /**
     * Configura a cadeia de filtros de segurança (SecurityFilterChain).
     *
     * @param http objeto `HttpSecurity` usado para personalizar as configurações de segurança.
     * @return a cadeia de filtros configurada.
     * @throws Exception em caso de erro na configuração de segurança.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilita proteção contra CSRF, adequada para APIs REST.
                .csrf(AbstractHttpConfigurer::disable)

                // Define que a aplicação não armazenará sessões de usuários (stateless).
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configura permissões de acesso para diferentes endpoints.
                .authorizeHttpRequests(req -> {
                    req.requestMatchers(HttpMethod.GET, "/topicos", "/topicos/*").permitAll(); // Permite GET em /topicos.
                    req.requestMatchers("/login").permitAll(); // Permite acesso ao login.
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll(); // Permite cadastro de usuários.
                    req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll(); // Permite acesso à documentação da API.
                    req.anyRequest().authenticated(); // Requer autenticação para outros endpoints.
                })

                // Adiciona o filtro de segurança personalizado antes do filtro padrão de autenticação.
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configura e retorna o gerenciador de autenticação (AuthenticationManager).
     *
     * @param configuration objeto `AuthenticationConfiguration` fornecido pelo Spring.
     * @return o gerenciador de autenticação configurado.
     * @throws Exception em caso de erro na obtenção do gerenciador de autenticação.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    /**
     * Define e retorna um codificador de senhas baseado no algoritmo BCrypt.
     *
     * @return instância de `PasswordEncoder` utilizando BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}