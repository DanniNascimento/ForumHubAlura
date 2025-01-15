package br.com.forumhub.ForumHub.service;


import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoResponse;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.infra.validacaoEtopico.atualizacao.ValidacaoAtualizacaoTopico;
import br.com.forumhub.ForumHub.infra.validacaoEtopico.criacao.ValidacaoCriacaodeTopico;
import br.com.forumhub.ForumHub.model.entities.Curso;
import br.com.forumhub.ForumHub.model.entities.Topico;
import br.com.forumhub.ForumHub.model.entities.Usuario;
import br.com.forumhub.ForumHub.model.enums.StatusTopico;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Serviço responsável pela gestão de tópicos no sistema.
 * Inclui criação, atualização, remoção, verificação e alteração de status de tópicos.
 */
@Service
public class TopicoService {

    // Injeção das dependências para acessar os repositórios e serviços relacionados ao tópico
    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private CursoService cursoService;

    @Autowired
    private UsuarioService usuarioService;

    // Listas de validações para criação e atualização de tópicos
    @Autowired
    private List<ValidacaoCriacaodeTopico> validacoesCriacao;

    @Autowired
    private List<ValidacaoAtualizacaoTopico> validacoesAtualizacao;

    /**
     * Cria um novo tópico no sistema.
     * Realiza as validações, associa o curso e o autor, e salva o tópico.
     *
     * @param cadastro dados do tópico para criação
     * @return dados do tópico criado
     */
    @Transactional
    public DadosTopicoResponse criarTopico(DadosTopicoCadastro cadastro) {

        // Verifica se o usuário atual está ativo antes de permitir a criação
        usuarioService.verificarSeUsuarioEstaAtivo();

        // Realiza as validações específicas para criação do tópico
        validacoesCriacao.forEach(validacao -> validacao.validar(cadastro));

        // Busca o curso pelo nome
        Curso curso = cursoService.buscarPorNome(cadastro.nomeCurso());

        // Obtém o usuário atual como autor
        Usuario autor = usuarioService.usuarioAtual();

        // Cria o novo tópico
        Topico topico = new Topico(cadastro, curso, autor);

        // Salva o tópico no repositório
        Topico newTopico = topicoRepository.save(topico);

        // Retorna a resposta com os dados do tópico criado
        return parseDadosTopicoResponse(newTopico);
    }

    /**
     * Converte um objeto Topico para DadosTopicoResponse.
     *
     * @param topico o tópico a ser convertido
     * @return os dados do tópico
     */
    private DadosTopicoResponse parseDadosTopicoResponse(Topico topico) {
        return new DadosTopicoResponse(topico);
    }

    /**
     * Busca um tópico pelo título e mensagem, ignorando maiúsculas e minúsculas.
     *
     * @param titulo título do tópico
     * @param mensagem mensagem do tópico
     * @return o tópico correspondente, ou null se não encontrado
     */
    public Topico buscarPorTituloEMensagem(String titulo, String mensagem) {
        return topicoRepository.findByTituloAndMensagemIgnoreCase(titulo, mensagem);
    }

    /**
     * Busca todos os tópicos no sistema, com base na paginação fornecida.
     *
     * @param paginacao os parâmetros de paginação
     * @return uma página de tópicos
     */
    public Page<Topico> buscarTodos(Pageable paginacao) {
        return topicoRepository.findAll(paginacao);
    }

    /**
     * Busca um tópico por seu ID.
     *
     * @param id o ID do tópico
     * @return o tópico correspondente, ou null se não encontrado
     */
    public Topico buscarPorId(Long id) {
        return topicoRepository.findById(id).orElse(null);
    }

    /**
     * Atualiza um tópico existente no sistema.
     * Realiza as validações e atualiza os dados do tópico.
     *
     * @param id o ID do tópico a ser atualizado
     * @param atualizacao os novos dados para atualizar o tópico
     * @return os dados do tópico atualizado
     */
    @Transactional
    public DadosTopicoResponse atualizarTopico(Long id, DadosTopicoAtualizacao atualizacao) {

        // Verifica se o usuário atual está ativo antes de permitir a atualização
        usuarioService.verificarSeUsuarioEstaAtivo();

        // Realiza as validações específicas para atualização do tópico
        validacoesAtualizacao.forEach(validacao -> validacao.validar(id, atualizacao));

        // Busca o tópico pelo ID e atualiza seus dados
        Topico topico = topicoRepository.findById(id).orElse(null);
        topico.atualizar(atualizacao);

        // Retorna a resposta com os dados do tópico atualizado
        return new DadosTopicoResponse(topico);
    }

    /**
     * Remove um tópico do sistema.
     * Verifica se o usuário é o autor do tópico antes de permitir a remoção.
     *
     * @param id o ID do tópico a ser removido
     */
    @Transactional
    public void removerTopico(Long id) {

        // Verifica se o usuário atual está ativo antes de permitir a remoção
        usuarioService.verificarSeUsuarioEstaAtivo();

        // Busca o tópico pelo ID
        Topico topico = topicoRepository.findById(id).orElse(null);

        // Verifica se o usuário atual é o autor do tópico antes de deletá-lo
        if (topico != null && topico.getAutor() != usuarioService.usuarioAtual()) {
            throw new ValidacaoException("Não foi possivel deletar o topico");
        }

        // Deleta o tópico do repositório
        topicoRepository.deleteById(id);
    }

    /**
     * Atualiza o status de um tópico.
     * Se o status for "Não Respondido", altera para "Não Solucionado", e vice-versa.
     * Se o status for "Não Solucionado", altera para "Solucionado".
     *
     * @param topico o tópico que terá seu status alterado
     */
    @Transactional
    public void atualizarStatusTopico(Topico topico) {
        // Verifica e altera o status do tópico conforme necessário
        if (topico.getStatus().equals(StatusTopico.NAO_RESPONDIDO)) {
            topico.setStatus(StatusTopico.NAO_SOLUCIONADO);
            return;
        }
        if (topico.getStatus().equals(StatusTopico.NAO_SOLUCIONADO)) {
            topico.setStatus(StatusTopico.SOLUCIONADO);
        }
    }

    /**
     * Decrementa o status do tópico, caso o número de respostas seja igual a 1.
     * Se for o caso, o status do tópico será alterado para "Não Respondido".
     *
     * @param topico o tópico que terá seu status decrementado
     */
    @Transactional
    public void decrementarStatus(Topico topico) {
        // Verifica se o tópico tem apenas uma resposta e altera seu status
        if(topico.getRespostas().size() == 1){
            topico.setStatus(StatusTopico.NAO_RESPONDIDO);
        }
    }
}
