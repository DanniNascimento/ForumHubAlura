package br.com.forumhub.ForumHub.dto.resposta;

import br.com.forumhub.ForumHub.model.entities.Resposta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Classe imutável que representa os dados de resposta a serem retornados para o cliente.
 * Este DTO (Data Transfer Object) é usado para transmitir informações sobre uma resposta.
 */
public record DadosRespostaResponse(
        Long id,               // Identificador único da resposta
        String mensagem,       // Mensagem/conteúdo da resposta
        String solucao,        // Solução fornecida na resposta (se aplicável)
        String nomeAutor,      // Nome do autor da resposta
        LocalDateTime dataCriacao // Data e hora de criação da resposta
) {
    /**
     * Construtor auxiliar que recebe uma entidade `Resposta` e mapeia seus dados para este record.
     *
     * @param resposta A entidade `Resposta` a ser convertida.
     */
    public DadosRespostaResponse(Resposta resposta) {
        this(
                resposta.getId(), // ID da resposta
                resposta.getMensagem(), // Mensagem da resposta
                resposta.getSolucao(), // Solução fornecida (se houver)
                resposta.getAutor() != null ? resposta.getAutor().getNome() : null, // Nome do autor (se presente)
                resposta.getDataCriacao() // Data de criação da resposta
        );
    }

    /**
     * Método estático para conversão de uma entidade `Resposta` em `DadosRespostaResponse`.
     *
     * @param resposta A entidade `Resposta` a ser convertida.
     * @return Uma instância de `DadosRespostaResponse` com os dados da resposta.
     */
    public static DadosRespostaResponse fromResposta(Resposta resposta) {
        return new DadosRespostaResponse(resposta); // Converte diretamente a entidade em um DTO
    }

}
