package br.com.forumhub.ForumHub.service;


import br.com.forumhub.ForumHub.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Serviço de autenticação responsável por carregar os detalhes do usuário
 * com base no e-mail fornecido.
 */
@Service
public class AutenticacaoService implements UserDetailsService {

    // Injeção do repositório de usuários para acesso ao banco de dados
    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Carrega o usuário com base no nome de usuário (e-mail).
     * Este método é usado pelo Spring Security para autenticação.
     *
     * @param username o e-mail do usuário
     * @return os detalhes do usuário encontrados no banco de dados
     * @throws UsernameNotFoundException se o e-mail não for encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username);
    }
}
