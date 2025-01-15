package br.com.forumhub.ForumHub.infra.security;


import br.com.forumhub.ForumHub.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.token.TokenService;
import br.com.forumhub.ForumHub.service.TokenService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de segurança que é executado uma vez por requisição.
 * Realiza a validação do token JWT e autentica o usuário no contexto de segurança.
 */
public class SecurityFilter extends OncePerRequestFilter {

    // Serviço para gerenciamento de tokens JWT.
    @Autowired
    private TokenService tokenService;

    // Repositório para buscar informações do usuário no banco de dados.
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Método principal do filtro, executado para cada requisição.
     *
     * @param request     a requisição HTTP.
     * @param response    a resposta HTTP.
     * @param filterChain a cadeia de filtros que será continuada após este filtro.
     * @throws ServletException em caso de erro de processamento da requisição.
     * @throws IOException      em caso de erro de entrada/saída.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        var tpkenJWT = recuperarToken(request);

        if (tpkenJWT != null){
            var subject = tokenService.getSujeito(tpkenJWT);
            var usuario = usuarioRepository.findByEmail(subject);

            var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(autenticacao);
        }

        filterChain.doFilter(request, response);
    }


    /**
     * Recupera o token JWT do cabeçalho "Authorization" da requisição.
     *
     * @param request a requisição HTTP.
     * @return o token JWT se presente e válido, ou {@code null} caso contrário.
     */
    private String recuperarToken(HttpServletRequest request) {
        var token = request.getHeader("Authorization");

        // Verifica se o cabeçalho está presente, não está vazio e começa com "Bearer ".
        if (token == null || token.isBlank() || !token.startsWith("Bearer ")) {
            return null;
        }

        // Remove o prefixo "Bearer " e retorna o token limpo.
        return token.replace("Bearer ", "");
    }
}
