package br.com.forumhub.ForumHub.model.entities;

import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaResponse;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoAtualizacao;
import br.com.forumhub.ForumHub.dto.topico.DadosTopicoCadastro;
import br.com.forumhub.ForumHub.model.enums.StatusTopico;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
//import org.springframework.data.annotation.Id;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "topicos") // Definindo o nome da tabela no banco de dados
@Entity(name = "Topico") // Entidade que será mapeada para a tabela
@EqualsAndHashCode(of = "id") // Equals e hashcode baseados apenas no 'id'
public class Topico {

    @Id // Identificador único do tópico
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Geração automática do ID
    private Long id;

    private String titulo; // Título do tópico
    private String mensagem; // Mensagem do tópico
    private LocalDateTime dataCriacao; // Data e hora de criação do tópico

    @Enumerated(EnumType.STRING) // Armazenamento do status como string no banco de dados
    private StatusTopico status;

    @ManyToOne(fetch = FetchType.LAZY) // Associação com a entidade 'Usuario', carregada de forma preguiçosa
    @JoinColumn(name = "autor_id") // Chave estrangeira para o autor do tópico
    private Usuario autor;

    @ManyToOne(fetch = FetchType.EAGER) // Associação com a entidade 'Curso', carregada de forma imediata
    @JoinColumn(name = "curso_id") // Chave estrangeira para o curso do tópico
    private Curso curso;

    @OneToMany(mappedBy = "topico", fetch = FetchType.LAZY) // Associação com a entidade 'Resposta', carregada de forma preguiçosa
    private List<Resposta> respostas = new ArrayList<>(); // Lista de respostas associadas ao tópico

    // Construtor para criação de um tópico com dados de cadastro
    public Topico(DadosTopicoCadastro cadastro, Curso curso, Usuario autor) {
        this.titulo = cadastro.titulo();
        this.mensagem = cadastro.mensagem();
        this.dataCriacao = LocalDateTime.now(); // Atribui a data e hora atual
        this.status = StatusTopico.NAO_RESPONDIDO; // Status inicial como 'Não Respondido'
        this.curso = curso;
        this.autor = autor;
    }

    // Construtor vazio (necessário para algumas operações como a persistência no banco de dados)
    public Topico() {
    }

    /**
     * Construtor para instanciar um tópico com dados completos.
     *
     * @param id ID do tópico.
     * @param titulo Título do tópico.
     * @param mensagem Mensagem do tópico.
     * @param dataCriacao Data de criação do tópico.
     * @param status Status do tópico.
     * @param autor Autor do tópico.
     * @param curso Curso associado ao tópico.
     * @param respostas Lista de respostas associadas ao tópico.
     */
    public Topico(Long id, String titulo, String mensagem, LocalDateTime dataCriacao, StatusTopico status, Usuario autor, Curso curso, List<Resposta> respostas) {
        this.id = id;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao;
        this.status = status;
        this.autor = autor;
        this.curso = curso;
        this.respostas = respostas;
    }

    // Métodos getters
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public StatusTopico getStatus() {
        return status;
    }

    public Usuario getAutor() {
        return autor;
    }

    public Curso getCurso() {
        return curso;
    }

    /**
     * Retorna as respostas associadas ao tópico transformadas em objetos 'DadosRespostaResponse'.
     */
    public List<DadosRespostaResponse> getRespostas() {
        return respostas.stream()
                .map(DadosRespostaResponse::new) // Mapeia cada resposta para um objeto 'DadosRespostaResponse'
                .toList();
    }

    /**
     * Atualiza os campos do tópico com os dados fornecidos.
     *
     * @param atualizacao Dados de atualização do tópico.
     */
    public void atualizar(DadosTopicoAtualizacao atualizacao) {
        if (atualizacao.titulo() != null) {
            this.titulo = atualizacao.titulo(); // Atualiza o título, se fornecido
        }

        if (atualizacao.mensagem() != null) {
            this.mensagem = atualizacao.mensagem(); // Atualiza a mensagem, se fornecida
        }
    }

    /**
     * Altera o status do tópico.
     *
     * @param statusTopico Novo status do tópico.
     */
    public void setStatus(StatusTopico statusTopico) {
        this.status = statusTopico; // Define o novo status para o tópico
    }
}
