package br.com.forumhub.ForumHub.model.entities;

import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaAtualizacao;
import br.com.forumhub.ForumHub.dto.resposta.DadosRespostaCadastro;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Representa uma resposta no sistema de fórum.
 * A entidade Resposta está associada a um tópico e um autor.
 */
@Table(name = "respostas")
@Entity(name = "Resposta")
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private String solucao; // Alterado para String

    /**
     * Construtor padrão sem argumentos.
     */
    public Resposta() {
    }

    /**
     * Construtor para inicializar uma nova resposta.
     *
     * @param dados  Dados da resposta a ser cadastrada.
     * @param topico Tópico ao qual a resposta pertence.
     * @param usuario Usuário que é o autor da resposta.
     */
    public Resposta(DadosRespostaCadastro dados, Topico topico, Usuario usuario) {
        this.mensagem = dados.mensagem();
        this.solucao = dados.solucao(); // Agora solucao é String
        this.topico = topico;
        this.autor = usuario;
        this.dataCriacao = LocalDateTime.now();
    }

    /**
     * Construtor completo para instanciar uma resposta.
     *
     * @param id           ID da resposta.
     * @param mensagem     Conteúdo da mensagem da resposta.
     * @param dataCriacao  Data de criação da resposta.
     * @param topico       Tópico ao qual a resposta pertence.
     * @param autor        Usuário autor da resposta.
     * @param solucao      Indica se a resposta resolve a dúvida (como String).
     */
    public Resposta(Long id, String mensagem, LocalDateTime dataCriacao, Topico topico, Usuario autor, String solucao) {
        this.id = id;
        this.mensagem = mensagem;
        this.dataCriacao = dataCriacao;
        this.topico = topico;
        this.autor = autor;
        this.solucao = solucao; // Agora solucao é String
    }

    // Getters necessários para o DTO DadosRespostaResponse
    public Long getId() {
        return id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public Topico getTopico() {
        return topico;
    }

    public Usuario getAutor() {
        return autor;
    }

    public String getSolucao() {
        return solucao; // Solucao como String
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao; // Solucao como String
    }

    /**
     * Atualiza os campos da resposta com os dados fornecidos.
     *
     * @param resposta Dados de atualização da resposta.
     */
    public void atualizar(DadosRespostaAtualizacao resposta) {
        if (resposta.mensagem() != null) {
            this.mensagem = resposta.mensagem();
        }

        if (resposta.solucao() != null) {
            this.solucao = resposta.solucao();
        }
    }
}