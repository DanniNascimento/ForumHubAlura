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

    // Injeção das dependências para acessar o repositório de usuários, tópicos e para a criptografia de senha
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retorna o usuário atualmente autenticado com base no email obtido do contexto de segurança.
     *
     * @return o usuário atual autenticado
     */
    public Usuario usuarioAtual() {
        // Obtém o e-mail do usuário autenticado a partir do SecurityContextHolder
        var email = (String) SecurityContextHolder.getContext().getAuthentication().getName();
        // Retorna o usuário correspondente ao e-mail
        return (Usuario) usuarioRepository.findByEmail(email);
    }

    /**
     * Cadastra um novo usuário no sistema.
     * Verifica se o e-mail já está cadastrado, criptografa a senha e salva o usuário no banco de dados.
     *
     * @param cadastro dados do usuário para cadastro
     * @return os dados do usuário cadastrado
     */
    @Transactional
    public DadosUsuarioResponse cadastrarUsuario(DadosCadastroUsuario cadastro) {

        // Verifica se já existe um usuário com o mesmo e-mail
        if (usuarioRepository.findByEmail(cadastro.email()) != null) {
            throw new ValidacaoException("Email já cadastrado");
        }

        // Criptografa a senha antes de salvar
        Usuario usuario = new Usuario(cadastro.nome(), cadastro.email(), criptografarSenha(cadastro.senha()));

        // Salva o novo usuário no banco de dados
        Usuario novoUsuario = usuarioRepository.save(usuario);

        // Cria um DadosUsuarioResponse a partir do novo Usuario
        return new DadosUsuarioResponse(novoUsuario);
    }

    /**
     * Atualiza as informações de um usuário, como nome e senha.
     * A senha é criptografada antes de ser atualizada.
     *
     * @param dados dados atualizados do usuário
     * @return os dados do usuário atualizado
     */
    @Transactional
    public DadosUsuarioResponse atualizarUsuario(DadosUsuarioAtualizacao dados) {

        // Verifica se o usuário está ativo antes de permitir a atualização
        verificarSeUsuarioEstaAtivo();

        // Obtém o usuário atual
        Usuario usuario = usuarioAtual();

        // Se uma nova senha for fornecida, ela será criptografada
        if (dados.senha() != null) {
            dados = dados.atualizarSenha(criptografarSenha(dados.senha()));
        }

        // Atualiza os dados do usuário
        usuario.atualizar(dados);

        // Retorna os dados do usuário atualizado
        return new DadosUsuarioResponse(usuario);
    }

    /**
     * Verifica se o usuário está ativo.
     * Caso contrário, lança uma exceção para indicar que a operação não é permitida.
     */
    public void verificarSeUsuarioEstaAtivo() {
        // Obtém o usuário atual
        Usuario usuario = usuarioAtual();
        // Se o usuário não estiver ativo, lança uma exceção
        if (!usuario.getAtivo()) {
            throw new ValidacaoException("OPERAÇÃO NÃO PERMITIDA: Usuário inativo");
        }
    }

    /**
     * Marca o usuário atual como deletado (delete lógico).
     */
    @Transactional
    public void deletar() {

        // Verifica se o usuário está ativo antes de permitir a exclusão
        verificarSeUsuarioEstaAtivo();

        // Obtém o usuário atual e marca como deletado
        Usuario usuario = usuarioAtual();
        usuario.deletar();
    }

    /**
     * Criptografa a senha utilizando o PasswordEncoder.
     *
     * @param senha a senha a ser criptografada
     * @return a senha criptografada
     */
    public String criptografarSenha(String senha) {
        // Retorna a senha criptografada
        return passwordEncoder.encode(senha);
    }

    /**
     * Retorna uma lista de usuários ativos com base na paginação fornecida.
     *
     * @param paginacao os parâmetros de paginação
     * @return uma página de usuários ativos
     */
    public Page<Usuario> buscarUsuario(Pageable paginacao) {
        // Busca os usuários ativos
        return usuarioRepository.findByAtivoTrue(paginacao);
    }

    /**
     * Retorna uma lista de tópicos criados pelo usuário atual, com base na paginação fornecida.
     *
     * @param paginacao os parâmetros de paginação
     * @return uma página de tópicos criados pelo usuário
     */
    public Page<Topico> buscarTopicos(Pageable paginacao) {
        // Busca os tópicos criados pelo usuário atual
        return topicoRepository.findByAutorId(usuarioAtual().getId(), paginacao);
    }
}