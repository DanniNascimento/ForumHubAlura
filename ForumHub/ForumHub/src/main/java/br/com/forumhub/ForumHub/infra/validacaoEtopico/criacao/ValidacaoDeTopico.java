package br.com.forumhub.ForumHub.infra.validacaoEtopico.criacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validação para verificar se já existe um tópico com o mesmo título e mensagem.
 */
@Component
public class ValidacaoDeTopico implements ValidacaoCriacaodeTopico {

    @Autowired
    private TopicoRepository topicoRepository;

    /**
     * Valida se já existe um tópico com o mesmo título e mensagem, lançando uma exceção caso positivo.
     *
     * @param dados Dados do cadastro do tópico.
     */
    @Override
    public void validar(DadosTopicoCadastro dados) {
        var topico = topicoRepository.findByTituloAndMensagemIgnoreCase(dados.titulo(), dados.mensagem());

        if (topico != null) {
            throw new ValidacaoException("Tópico já existente: " + dados.titulo());
        }
    }
}