package br.com.forumhub.ForumHub.controller;

import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaAtualizacao;
import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaCadastro;
import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaResponse;
import br.com.forumhub.ForumHub.service.RespostaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/respostas") // Define o URL base para os endpoints relacionados às respostas
@SecurityRequirement(name = "bearer-key") // Exige autenticação via token Bearer para todas as operações
@Tag(name = "Respostas", description = "Endpoints relacionados à gestão de respostas em tópicos.") // Descrição da API para o Swagger
public class RespostaController {

    private final RespostaService respostaService; // Serviço que gerencia as respostas

    // Injeção de dependência via construtor
    @Autowired
    public RespostaController(RespostaService respostaService) {
        this.respostaService = respostaService;
    }

    /**
     * Publica uma nova resposta em um tópico específico.
     * Cada vez que uma resposta é criada, o status do tópico é atualizado.
     *
     * @param resposta dados da resposta a ser criada.
     * @param id identificador do tópico onde a resposta será postada.
     * @param uriBuilder objeto utilizado para construir a URI da nova resposta.
     * @return ResponseEntity com a URI da resposta criada e os dados da mesma.
     */
    @PostMapping("/{id}")
    @Operation(
            summary = "Postar Resposta",
            description = "Realiza a publicação de uma resposta em um tópico. Atualiza o status do tópico automaticamente."
    )
    public ResponseEntity<DadosRespostaResponse> criar(
            @RequestBody @Valid DadosRespostaCadastro resposta, // Dados da nova resposta recebidos no corpo da requisição
            @PathVariable Long id, // ID do tópico onde a resposta será postada
            UriComponentsBuilder uriBuilder) {

        // Chama o serviço para criar a resposta e atualizar o tópico automaticamente
        DadosRespostaResponse respostaCriada = respostaService.criar(resposta, id);

        // Constrói a URI do recurso criado com o ID da resposta
        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(respostaCriada.id()).toUri();

        // Retorna a resposta HTTP com código 201 (Criado) e a URI da resposta criada
        return ResponseEntity.created(uri).body(respostaCriada);
    }

    /**
     * Atualiza os dados de uma resposta específica.
     *
     * @param resposta dados atualizados da resposta.
     * @param id identificador da resposta a ser atualizada.
     * @return ResponseEntity com os dados da resposta atualizada.
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Resposta", description = "Atualiza uma resposta específica por ID.")
    public ResponseEntity<DadosRespostaResponse> atualizar(
            @RequestBody @Valid DadosRespostaAtualizacao resposta, // Dados atualizados da resposta
            @PathVariable Long id) { // ID da resposta a ser atualizada

        // Chama o serviço para atualizar a resposta com o ID correspondente
        DadosRespostaResponse respostaAtualizada = respostaService.atualizar(resposta, id);

        // Retorna a resposta HTTP com código 200 (OK) e os dados da resposta atualizada
        return ResponseEntity.ok(respostaAtualizada);
    }

    /**
     * Deleta uma resposta específica por ID.
     * Caso seja a última resposta do tópico, o status do tópico será atualizado para 'NAO_RESPONDIDO'.
     *
     * @param id identificador da resposta a ser deletada.
     * @return ResponseEntity sem corpo indicando sucesso na operação.
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Deletar Resposta",
            description = "Remove uma resposta específica. Atualiza o status do tópico se for a última resposta."
    )
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        // Chama o serviço para deletar a resposta e atualizar o status do tópico, se necessário
        respostaService.deletar(id);

        // Retorna uma resposta HTTP com código 200 (OK) indicando que a operação foi bem-sucedida
        return ResponseEntity.ok().build();
    }
}
