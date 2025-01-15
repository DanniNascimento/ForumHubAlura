package br.com.forumhub.ForumHub.dto.usuario;

import br.com.forumhub.ForumHub.model.entities.Usuario;

public class DadosUsuarioResponse {

    private Long id;     // ID do usuário
    private String nome; // Nome do usuário
    private String email; // Email do usuário

    /**
     * Construtor para criar um DTO a partir da entidade Usuario.
     * @param usuario A entidade Usuario a ser convertida.
     */
    public DadosUsuarioResponse(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
    }

    // Getters para os campos (ou use Lombok para gerar automaticamente)
    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
