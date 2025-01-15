package br.com.forumhub.ForumHub.repository;

import br.com.forumhub.ForumHub.model.entities.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositório para a entidade Curso, responsável por interagir com o banco de dados.
 */
public interface CursoRepository extends JpaRepository<Curso, Long> {

    /**
     * Busca um curso pelo nome, ignorando diferenças entre maiúsculas e minúsculas.
     *
     * @param nome o nome do curso a ser buscado.
     * @return o curso encontrado ou null caso nenhum curso corresponda.
     */
    Curso findByNomeIgnoreCase(String nome);
}