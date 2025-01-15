package br.com.forumhub.ForumHub.dto.topico;

import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaResponse;
import br.com.forumhub.ForumHub.model.entities.Topico;
import br.com.forumhub.ForumHub.model.enums.StatusTopico;

import java.util.List;

/**
 * Classe imutável que representa os detalhes de um tópico, incluindo informações básicas
 * e as respostas associadas. Utiliza o recurso de records do Java para simplificar a criação
 * de classes DTO (Data Transfer Object).
 */
public record DadosTopicoDetalhado(
        Long id,                       // Identificador único do tópico
        String titulo,                 // Título do tópico
        String mensagem,               // Mensagem/conteúdo do tópico
        String nomeAutor,              // Nome do autor do tópico
        StatusTopico status,           // Status atual do tópico (e.g., NÃO_RESPONDIDO, SOLUCIONADO)
        List<DadosRespostaResponse> respostas // Lista de respostas associadas ao tópico
) {

    /**
     * Construtor auxiliar que recebe uma entidade `Topico` e mapeia seus dados para este record.
     *
     * Converte a entidade do tipo `Topico` em um DTO `DadosTopicoDetalhado` e suas respostas em DTOs.
     *
     * @param topico A entidade `Topico` a ser convertida
     */
    public DadosTopicoDetalhado(Topico topico) {
        this(
                topico.getId(), // ID do tópico
                topico.getTitulo(), // Título do tópico
                topico.getMensagem(), // Mensagem do tópico
                topico.getAutor() != null ? topico.getAutor().getNome() : null, // Nome do autor do tópico (se presente)
                topico.getStatus(), // Status do tópico
                topico.getRespostas() // Usando o método getRespostas() diretamente para obter as respostas como DTOs
        );
    }
}