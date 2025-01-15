package br.com.forumhub.ForumHub.controller;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoResponse;
import br.com.forumhub.ForumHub.dto.usuario.DadosCadastroUsuario;
import br.com.forumhub.ForumHub.dto.usuario.DadosNome;
import br.com.forumhub.ForumHub.dto.usuario.DadosUsuarioAtualizacao;
import br.com.forumhub.ForumHub.dto.usuario.DadosUsuarioResponse;
import br.com.forumhub.ForumHub.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/usuarios") // Mapeia o caminho base da URL para os endpoints de usuários
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários no fórum.") // Descrição para o Swagger
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Dependência do serviço que lida com a lógica de usuários

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Realiza o cadastro de um novo usuário no fórum.
     *
     * @param cadastro dados do novo usuário.
     * @param uriBuilder objeto para construir a URI do recurso criado.
     * @return ResponseEntity com os dados do usuário cadastrado.
     */
    @PostMapping
    @Operation(summary = "Cadastrar Usuário", description = "Realiza o cadastro de um novo usuário no fórum.")
    public ResponseEntity<?> cadastrarUsuario(
            @RequestBody @Valid DadosCadastroUsuario cadastro, // Dados de cadastro recebidos no corpo da requisição
            UriComponentsBuilder uriBuilder) {

        // Chama o serviço para cadastrar o usuário e cria a URI do novo recurso
        var usuarioCadastrado = usuarioService.cadastrarUsuario(cadastro);

        // Cria a URI com o ID do usuário
        var uri = uriBuilder.path("/usuarios").build().toUri();

        // Retorna a resposta com status 201 (Criado) e os dados do usuário cadastrado
        return ResponseEntity.created(uri).body(usuarioCadastrado);
    }

    /**
     * Atualiza os dados do usuário logado (nome e senha).
     *
     * @param dados informações atualizadas do usuário.
     * @return ResponseEntity com os dados do usuário atualizado.
     */
    @PutMapping
    @SecurityRequirement(name = "bearer-key") // Exige autenticação por token Bearer
    @Operation(summary = "Atualizar Usuário", description = "Atualiza os dados de nome e senha do usuário logado.")
    public ResponseEntity<DadosUsuarioResponse> atualizarUsuario(
            @RequestBody @Valid DadosUsuarioAtualizacao dados) { // Dados de atualização recebidos no corpo da requisição

        // Chama o serviço para atualizar os dados do usuário
        var usuarioAtualizado = usuarioService.atualizarUsuario(dados);
        return ResponseEntity.ok(usuarioAtualizado); // Retorna os dados atualizados com status 200 (OK)
    }

    /**
     * Torna o usuário logado inativo (delete lógico).
     *
     * @return ResponseEntity sem corpo indicando sucesso na operação.
     */
    @DeleteMapping
    @SecurityRequirement(name = "bearer-key") // Exige autenticação por token Bearer
    @Operation(summary = "Deletar Usuário", description = "Torna o usuário inativo no banco de dados (delete lógico).")
    public ResponseEntity<Void> removerUsuario() {
        usuarioService.deletar(); // Chama o serviço para realizar o delete lógico
        return ResponseEntity.ok().build(); // Retorna status 200 (OK) sem corpo
    }

    /**
     * Lista todos os usuários cadastrados no fórum de forma paginada.
     *
     * @param paginacao informações de paginação.
     * @return ResponseEntity contendo a página de usuários.
     */
    @GetMapping
    @SecurityRequirement(name = "bearer-key") // Exige autenticação por token Bearer
    @Operation(summary = "Listar Usuários", description = "Lista todos os usuários cadastrados no fórum.")
    public ResponseEntity<Page<DadosNome>> buscarUsuario(
            @PageableDefault(size = 10) Pageable paginacao) { // Parâmetro de paginação (tamanho de página padrão: 10)

        // Chama o serviço para buscar os usuários e os converte para o DTO DadosNome
        var page = usuarioService.buscarUsuario(paginacao).map(DadosNome::new);
        return ResponseEntity.ok(page); // Retorna a página de usuários com status 200 (OK)
    }

    /**
     * Lista todos os tópicos postados pelo usuário logado.
     *
     * @param paginacao informações de paginação e ordenação.
     * @return ResponseEntity contendo a página de tópicos.
     */
    @GetMapping("/topicos")
    @SecurityRequirement(name = "bearer-key") // Exige autenticação por token Bearer
    @Operation(
            summary = "Listar Tópicos do Usuário",
            description = "Lista todos os tópicos postados pelo usuário logado, ordenados por data de publicação."
    )
    public ResponseEntity<Page<DadosTopicoResponse>> listarTopicos(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) { // Paginação com ordenação por data de criação

        // Chama o serviço para buscar os tópicos do usuário logado e os converte para o DTO DadosTopicoResponse
        var page = usuarioService.buscarTopicos(paginacao).map(DadosTopicoResponse::new);
        return ResponseEntity.ok(page); // Retorna a página de tópicos com status 200 (OK)
    }
}
