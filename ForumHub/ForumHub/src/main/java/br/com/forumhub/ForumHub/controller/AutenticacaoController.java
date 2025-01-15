package br.com.forumhub.ForumHub.controller;

import br.com.forumhub.ForumHub.dto.token.DadosTokenJWT;
import br.com.forumhub.ForumHub.dto.usuario.DadosAutenticacao;
import br.com.forumhub.ForumHub.model.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import br.com.forumhub.ForumHub.service.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")  // Mapeia a URL base para a autenticação (login)
@Tag(name = "Autenticação/Login", description = "Endpoints relacionados à autenticação e geração de tokens JWT")  // Descrição da API para o Swagger
public class AutenticacaoController {

    private final AuthenticationManager authenticationManager;  // Gerencia a autenticação
    private final TokenService tokenService;  // Serviço para gerar tokens JWT

    // Injeção de dependência via construtor
    @Autowired
    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    /**
     * Efetua o login e gera um Token JWT para autenticação das requisições.
     *
     * @param dados dados de autenticação (e-mail e senha) enviados pelo cliente.
     * @return um ResponseEntity contendo o Token JWT gerado.
     */
    @PostMapping  // Endpoint HTTP POST para o login
    @Operation(
            summary = "Efetuar login",  // Resumo da operação
            description = "Gera um Token JWT para ser utilizado na autenticação das requisições subsequentes."  // Descrição do que a operação faz
    )
    public ResponseEntity<DadosTokenJWT> efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        // Criação do token de autenticação usando o e-mail e senha fornecidos
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());

        // Realiza a autenticação do usuário com as credenciais fornecidas
        var authentication = authenticationManager.authenticate(authenticationToken);

        // Geração do Token JWT baseado no usuário autenticado
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // Retorna o token JWT gerado como resposta com status HTTP 200 (OK)
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
