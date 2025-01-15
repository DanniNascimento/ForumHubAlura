package br.com.forumhub.ForumHub.infra.validacaoEtopico.criacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;

public interface ValidacaoCriacaodeTopico {

    void validar(DadosTopicoCadastro dados);
}
