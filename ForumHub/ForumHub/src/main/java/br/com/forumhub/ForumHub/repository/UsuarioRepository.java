package br.com.forumhub.ForumHub.repository;


import br.com.forumhub.ForumHub.model.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Repositório para a entidade Usuario, responsável por realizar operações no banco de dados relacionadas aos usuários.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca os detalhes de um usuário pelo e-mail.
     *
     * @param email o e-mail do usuário a ser buscado.
     * @return os detalhes do usuário correspondente ou null caso nenhum usuário seja encontrado.
     */
    UserDetails findByEmail(String email);

    /**
     * Busca todos os usuários ativos, com suporte a paginação.
     *
     * @param paginacao informações de paginação.
     * @return uma página contendo os usuários ativos.
     */
    Page<Usuario> findByAtivoTrue(Pageable paginacao);

}
