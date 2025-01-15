package br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao;


import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;

/**
 * Validação para garantir que pelo menos um campo seja informado para a atualização de um tópico.
 */
public class ValidacaoDeCampo implements ValidacaoAtualizacaoTopico {

    /**
     * Valida se ao menos um campo (título ou mensagem) foi informado na atualização.
     *
     * @param id    ID do tópico a ser atualizado.
     * @param dados Dados da atualização do tópico.
     */
    @Override
    public void validar(Long id, DadosTopicoAtualizacao dados) {
        if (dados.titulo() == null && dados.mensagem() == null) {
            throw new ValidacaoException("É necessário informar ao menos um campo para atualização.");
        }
    }
}