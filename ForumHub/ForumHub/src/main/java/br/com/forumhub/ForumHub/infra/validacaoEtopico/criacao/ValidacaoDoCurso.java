package br.com.forumhub.ForumHub.infra.validacaoEtopico.criacao;

import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Validação para verificar se o curso informado no cadastro do tópico existe.
 */
@Component
public class ValidacaoDoCurso implements ValidacaoCriacaodeTopico {

    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Valida se o curso informado existe no repositório, lançando uma exceção caso contrário.
     *
     * @param dados Dados do cadastro do tópico.
     */
    @Override
    public void validar(DadosTopicoCadastro dados) {
        var curso = cursoRepository.findByNomeIgnoreCase(dados.nomeCurso());

        if (curso == null) {
            throw new ValidacaoException("Curso não encontrado: " + dados.nomeCurso());
        }
    }
}