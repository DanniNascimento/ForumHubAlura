package br.com.forumhub.ForumHub.service;

import br.com.forumhub.ForumHub.model.entities.Usuario;
import com.auth0.jwt.JWT;  // Importação correta
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Serviço responsável pela geração e validação de tokens JWT.
 * Utiliza o algoritmo HMAC256 para assinatura e validação dos tokens.
 */
@Service
public class TokenService {

    // Chave secreta para gerar o token, extraída das configurações da aplicação
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para o usuário fornecido.
     * O token é assinado com a chave secreta e tem um tempo de expiração de 2 horas.
     *
     * @param usuario o usuário para o qual o token será gerado
     * @return o token JWT gerado
     */
    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    public String gerarToken(Usuario usuario) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            logger.info("Gerando token para o usuário: {}", usuario.getEmail());

            return JWT.create()
                    .withIssuer("ForumHub")
                    .withSubject(usuario.getEmail())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (Exception e) {
            logger.error("Erro ao gerar token para o usuário: {}", usuario.getEmail(), e);
            throw new RuntimeException("Erro ao gerar token", e);
        }
    }

    /**
     * Define a data de expiração do token.
     * O token expira após 2 horas.
     *
     * @return a data de expiração do token
     */
    private Instant dataExpiracao() {
        // Adiciona 2 horas à data e hora atuais para definir a expiração do token
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSujeito(String tpkenJWT) {
        try{
            return JWT.require(Algorithm.HMAC256(secret))
                    .withIssuer("ForumHub")
                    .build()
                    .verify(tpkenJWT)
                    .getSubject();
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao verificar token", exception);
        }
    }

}