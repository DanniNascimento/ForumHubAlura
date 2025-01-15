package br.com.forumhub.ForumHub.controller;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoDetalhado;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoResponse;
import br.com.forumhub.ForumHub.service.TopicoService;
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
@RequestMapping("/topicos") // Define a URL base para os endpoints relacionados a tópicos
@Tag(name = "Tópicos", description = "Endpoints para gerenciamento de tópicos no fórum.") // Descrição para o Swagger
public class TopicoController {

    private final TopicoService topicoService; // Dependência do serviço que gerencia a lógica dos tópicos

    // Injeção de dependência via construtor
    @Autowired
    public TopicoController(TopicoService topicoService) {
        this.topicoService = topicoService;
    }

    /**
     * Publica um novo tópico no fórum.
     *
     * @param cadastro dados do tópico a ser criado.
     * @param uriBuilder objeto utilizado para construir a URI do tópico criado.
     * @return ResponseEntity com a URI do tópico criado e os dados do mesmo.
     */
    @PostMapping
    @SecurityRequirement(name = "bearer-key") // Exige autenticação via token Bearer
    @Operation(summary = "Postar Tópico", description = "Realiza a publicação de um tópico no fórum.")
    public ResponseEntity<DadosTopicoResponse> criarTopico(
            @RequestBody @Valid DadosTopicoCadastro cadastro, // Dados do novo tópico recebidos no corpo da requisição
            UriComponentsBuilder uriBuilder) {

        // Chama o serviço para criar o tópico e gera a URI do tópico criado
        var topicoResponse = topicoService.criarTopico(cadastro);
        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topicoResponse.getId()).toUri();

        // Retorna uma resposta HTTP 201 (Criado) com a URI e dados do tópico
        return ResponseEntity.created(uri).body(topicoResponse);
    }

    /**
     * Lista todos os tópicos postados no fórum de forma paginada.
     *
     * @param paginacao informações de paginação e ordenação.
     * @return ResponseEntity contendo a página de tópicos.
     */
    @GetMapping
    @Operation(
            summary = "Listar Todos os Tópicos Postados",
            description = "Lista todos os tópicos postados no fórum, ordenados por data de publicação em páginas de 10 itens."
    )
    public ResponseEntity<Page<DadosTopicoResponse>> listarTopicos(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) { // Parâmetros de paginação padrão

        // Chama o serviço para buscar todos os tópicos e os converte para o DTO DadosTopicoResponse
        var page = topicoService.buscarTodos(paginacao).map(DadosTopicoResponse::new);
        return ResponseEntity.ok(page); // Retorna a página de tópicos com status 200 (OK)
    }

    /**
     * Busca um tópico específico por ID.
     *
     * @param id identificador do tópico.
     * @return ResponseEntity com os detalhes do tópico ou um erro 404 caso não seja encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar Tópico por ID", description = "Detalha um tópico específico por ID.")
    public ResponseEntity<DadosTopicoDetalhado> buscarPorId(@PathVariable Long id) {
        // Chama o serviço para buscar o tópico por ID
        var topico = topicoService.buscarPorId(id);
        if (topico == null) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o tópico não for encontrado
        }
        return ResponseEntity.ok(new DadosTopicoDetalhado(topico)); // Retorna os detalhes do tópico com status 200 (OK)
    }

    /**
     * Atualiza as informações de um tópico existente.
     *
     * @param id identificador do tópico a ser atualizado.
     * @param atualizacao dados atualizados do tópico.
     * @return ResponseEntity com os dados do tópico atualizado.
     */
    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearer-key") // Exige autenticação via token Bearer
    @Operation(
            summary = "Atualizar/Editar Tópico por ID",
            description = "Atualiza as informações publicadas em um tópico selecionando-o pelo ID."
    )
    public ResponseEntity<DadosTopicoResponse> atualizarTopico(
            @PathVariable Long id, // ID do tópico a ser atualizado
            @RequestBody @Valid DadosTopicoAtualizacao atualizacao) { // Dados de atualização recebidos no corpo da requisição

        // Chama o serviço para atualizar o tópico e retorna a resposta com os dados do tópico atualizado
        var topicoAtualizado = topicoService.atualizarTopico(id, atualizacao);
        return ResponseEntity.ok(topicoAtualizado); // Retorna os dados do tópico atualizado com status 200 (OK)
    }

    /**
     * Remove um tópico do fórum.
     *
     * @param id identificador do tópico a ser removido.
     * @return ResponseEntity sem corpo indicando sucesso na operação.
     */
    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearer-key") // Exige autenticação via token Bearer
    @Operation(
            summary = "Remover Tópico por ID",
            description = "Remove um tópico específico do fórum pelo ID, desde que tenha sido publicado pelo mesmo usuário."
    )
    public ResponseEntity<Void> removerTopico(@PathVariable Long id) {
        // Chama o serviço para remover o tópico
        topicoService.removerTopico(id);
        return ResponseEntity.ok().build(); // Retorna status 200 (OK) sem corpo
    }
}
