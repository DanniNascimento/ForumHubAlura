package br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validação para verificar se já existe um tópico com o mesmo título e mensagem.
 */
public class ValidacaoDaAtualizacaoDeTopico implements ValidacaoAtualizacaoTopico {

    @Autowired
    private TopicoRepository topicoRepository;

    /**
     * Valida se já existe um tópico com o mesmo título e mensagem, lançando uma exceção caso positivo.
     *
     * @param id    ID do tópico a ser atualizado.
     * @param dados Dados da atualização do tópico.
     */
    @Override
    public void validar(Long id, DadosTopicoAtualizacao dados) {
        var topico = topicoRepository.findByTituloAndMensagemIgnoreCase(dados.titulo(), dados.mensagem());

        if (topico != null) {
            throw new ValidacaoException("Já existe um tópico com o mesmo título e mensagem.");
        }
    }
}