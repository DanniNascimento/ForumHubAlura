package br.com.forumhub.ForumHub.dto.topico;


import br.com.forumhub.ForumHub.model.entities.Topico;

import java.time.LocalDateTime;

/**
 * Classe imutável que representa uma resposta básica sobre um tópico.
 * Utiliza o recurso de records do Java para simplificar a criação
 * de classes DTO.
 */
public record DadosTopicoResponse(
        Long id,                 // Identificador único do tópico
        String titulo,           // Título do tópico
        String mensagem,         // Mensagem/conteúdo do tópico
        LocalDateTime dataCriacao // Data e hora de criação do tópico
) {

    /**
     * Construtor auxiliar que converte uma entidade `Topico` em uma instância
     * deste record, mapeando os campos relevantes.
     *
     * @param topico a entidade `Topico` a ser convertida
     */
    public DadosTopicoResponse(Topico topico) {
        this(
                topico.getId(),          // ID do tópico
                topico.getTitulo(),      // Título do tópico
                topico.getMensagem(),    // Mensagem do tópico
                topico.getDataCriacao()  // Data de criação do tópico
        );
    }

    public Long getId() {
        return id;
    }
}

