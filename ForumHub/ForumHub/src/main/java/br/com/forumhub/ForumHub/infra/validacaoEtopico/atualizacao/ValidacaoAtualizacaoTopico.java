package br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;

public interface ValidacaoAtualizacaoTopico {
    void validar(Long id, DadosTopicoAtualizacao dados);
}
