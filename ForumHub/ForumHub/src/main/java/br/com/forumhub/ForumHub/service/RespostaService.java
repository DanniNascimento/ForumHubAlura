package br.com.forumhub.ForumHub.service;

import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaAtualizacao;
import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaCadastro;
import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaResponse;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.model.entities.Resposta;
import br.com.forumhub.ForumHub.repository.RespostaRepository;
import br.com.forumhub.ForumHub.service.TopicoService;
import br.com.forumhub.ForumHub.service.UsuarioService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RespostaService {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TopicoService topicoService;

    public Optional<DadosRespostaResponse> buscarDtoPorId(Long id) {
        return respostaRepository.findById(id)
                .map(DadosRespostaResponse::fromResposta); // Correto: converte a entidade Resposta para o DTO
    }

    @Transactional
    public DadosRespostaResponse criar(DadosRespostaCadastro dados, Long id) {
        usuarioService.verificarSeUsuarioEstaAtivo();

        var topico = Optional.ofNullable(topicoService.buscarPorId(id))
                .orElseThrow(() -> new ValidacaoException("Tópico não encontrado"));

        topicoService.atualizarStatusTopico(topico);
        var usuario = usuarioService.usuarioAtual();

        var respostaCriada = new Resposta(dados, topico, usuario);
        var respostaSalva = respostaRepository.save(respostaCriada);

        // Converta a entidade Resposta para o DTO DadosRespostaResponse
        return DadosRespostaResponse.fromResposta(respostaSalva);
    }

    @Transactional
    public DadosRespostaResponse atualizar(DadosRespostaAtualizacao dadosAtualizacao, Long id) {
        usuarioService.verificarSeUsuarioEstaAtivo();

        var resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada"));

        verificarUsuario(resposta);
        resposta.atualizar(dadosAtualizacao);

        respostaRepository.save(resposta);
        return DadosRespostaResponse.fromResposta(resposta);
    }

    private void verificarUsuario(Resposta resposta) {
        if (!usuarioService.usuarioAtual().equals(resposta.getAutor())) {
            throw new ValidacaoException("Você não tem permissão para fazer essa operação");
        }
    }

    @Transactional
    public void deletar(Long id) {
        usuarioService.verificarSeUsuarioEstaAtivo();

        var resposta = respostaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException("Resposta não encontrada"));

        verificarUsuario(resposta);
        respostaRepository.delete(resposta);

        topicoService.decrementarStatus(resposta.getTopico());
    }
}