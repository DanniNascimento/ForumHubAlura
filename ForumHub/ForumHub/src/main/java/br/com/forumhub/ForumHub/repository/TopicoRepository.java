package br.com.forumhub.ForumHub.repository;


import br.com.forumhub.ForumHub.model.entities.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade Topico, responsável por realizar operações no banco de dados relacionadas aos tópicos.
 */
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    /**
     * Busca um tópico pelo título e mensagem, ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param titulo o título do tópico a ser buscado.
     * @param mensagem a mensagem do tópico a ser buscada.
     * @return o tópico encontrado ou null caso nenhum tópico corresponda.
     */
    Topico findByTituloAndMensagemIgnoreCase(String titulo, String mensagem);

    /**
     * Lista todos os tópicos paginados.
     *
     * @param paginacao informações de paginação.
     * @return uma página contendo os tópicos encontrados.
     */
    Page<Topico> findAll(Pageable paginacao);

    /**
     * Busca os tópicos criados por um autor específico, com suporte a paginação.
     *
     * @param id o ID do autor dos tópicos.
     * @param paginacao informações de paginação.
     * @return uma página contendo os tópicos do autor especificado.
     */
    Page<Topico> findByAutorId(Long id, Pageable paginacao);

}
