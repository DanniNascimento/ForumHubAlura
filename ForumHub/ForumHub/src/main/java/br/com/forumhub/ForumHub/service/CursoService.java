package br.com.forumhub.ForumHub.service;


import br.com.forumhub.ForumHub.model.entities.Curso;
import br.com.forumhub.ForumHub.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de negócios relacionada aos cursos.
 * Interage com o repositório de cursos para realizar operações no banco de dados.
 */
@Service
public class CursoService {

    // Injeção do repositório de cursos para acessar os dados
    @Autowired
    private CursoRepository cursoRepository;

    /**
     * Busca um curso pelo ID.
     * Se o curso não for encontrado, retorna null.
     *
     * @param id o ID do curso a ser buscado
     * @return o curso encontrado ou null se não encontrado
     */
    public Curso buscarPorId(Long id) {
        return cursoRepository.findById(id).orElse(null);
    }

    /**
     * Busca um curso pelo nome, ignorando a diferença entre maiúsculas e minúsculas.
     *
     * @param nome o nome do curso a ser buscado
     * @return o curso encontrado ou null se não encontrado
     */
    public Curso buscarPorNome(String nome) {
        return cursoRepository.findByNomeIgnoreCase(nome);
    }
}
