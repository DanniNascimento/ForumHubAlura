package br.com.forumhub.ForumHub.dto.usuario;

import br.com.forumhub.ForumHub.model.entities.Usuario;

public record DadosNome(String nome) {

    public DadosNome(Usuario usuario) {
        this(usuario.getNome());
    }
}