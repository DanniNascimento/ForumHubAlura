package br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validação para verificar se o tópico informado existe.
 */
public class ValidacaoDeExistenciaTopico implements ValidacaoAtualizacaoTopico {

    @Autowired
    private TopicoRepository topicoRepository;

    /**
     * Valida se o tópico existe no repositório, lançando uma exceção caso contrário.
     *
     * @param id    ID do tópico a ser validado.
     * @param dados Dados da atualização do tópico.
     */
    @Override
    public void validar(Long id, DadosTopicoAtualizacao dados) {
        var topico = topicoRepository.findById(id);

        if (topico.isEmpty()) {
            throw new ValidacaoException("Informe um ID de tópico válido.");
        }
    }
}
