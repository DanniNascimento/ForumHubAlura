package br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao;


import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.model.entities.Topico;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import br.com.forumhub.ForumHub.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validação para verificar se o usuário atual tem permissão para atualizar o tópico.
 */
public class ValidacaoUsuarioAtualizacao implements ValidacaoAtualizacaoTopico {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Valida se o usuário atual é o autor do tópico a ser atualizado.
     *
     * @param id    ID do tópico a ser validado.
     * @param dados Dados da atualização do tópico.
     */
    @Override
    public void validar(Long id, DadosTopicoAtualizacao dados) {
        Topico topico = topicoRepository.findById(id).orElseThrow(() ->
                new ValidacaoException("Tópico não encontrado para o ID informado.")
        );

        if (!topico.getAutor().equals(usuarioService.usuarioAtual())) {
            throw new ValidacaoException("Usuário não autorizado para atualizar o tópico.");
        }
    }
}
