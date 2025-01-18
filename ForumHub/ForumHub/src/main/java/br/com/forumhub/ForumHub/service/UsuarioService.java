package br.com.forumhub.ForumHub.service;


import br.com.forumhub.ForumHub.dto.usuario.DadosCadastroUsuario;
import br.com.forumhub.ForumHub.dto.usuario.DadosUsuarioAtualizacao;
import br.com.forumhub.ForumHub.dto.usuario.DadosUsuarioResponse;
import br.com.forumhub.ForumHub.infra.exception.ValidacaoException;
import br.com.forumhub.ForumHub.model.entities.Topico;
import br.com.forumhub.ForumHub.model.entities.Usuario;
import br.com.forumhub.ForumHub.repository.TopicoRepository;
import br.com.forumhub.ForumHub.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela gestão de usuários no sistema.
 * Inclui cadastro, atualização, exclusão, e verificação de status de usuário.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario usuarioAtual() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ValidacaoException("Usuário não encontrado"));
    }

    @Transactional
    public DadosUsuarioResponse cadastrarUsuario(DadosCadastroUsuario cadastro) {
        if (usuarioRepository.findByEmail(cadastro.email()).isPresent()) {
            throw new ValidacaoException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(
                cadastro.nome(),
                cadastro.email(),
                criptografarSenha(cadastro.senha())
        );

        Usuario novoUsuario = usuarioRepository.save(usuario);
        return new DadosUsuarioResponse(novoUsuario);
    }

    @Transactional
    public DadosUsuarioResponse atualizarUsuario(DadosUsuarioAtualizacao dados) {
        verificarSeUsuarioEstaAtivo();

        Usuario usuario = usuarioAtual();

        if (dados.senha() != null) {
            dados = dados.atualizarSenha(criptografarSenha(dados.senha()));
        }

        usuario.atualizar(dados);
        return new DadosUsuarioResponse(usuario);
    }

    public void verificarSeUsuarioEstaAtivo() {
        Usuario usuario = usuarioAtual();
        if (!usuario.getAtivo()) {
            throw new ValidacaoException("OPERAÇÃO NÃO PERMITIDA: Usuário inativo");
        }
    }

    @Transactional
    public void deletar() {
        verificarSeUsuarioEstaAtivo();
        Usuario usuario = usuarioAtual();
        usuario.deletar();
    }

    public String criptografarSenha(String senha) {
        return passwordEncoder.encode(senha);
    }

    public Page<Usuario> buscarUsuario(Pageable paginacao) {
        return usuarioRepository.findByAtivoTrue(paginacao);
    }

    public Page<Topico> buscarTopicos(Pageable paginacao) {
        return topicoRepository.findByAutorId(usuarioAtual().getId(), paginacao);
    }
}