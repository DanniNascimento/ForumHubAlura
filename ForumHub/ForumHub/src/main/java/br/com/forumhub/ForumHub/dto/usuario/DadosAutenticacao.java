package br.com.forumhub.ForumHub.dto.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class DadosAutenticacao {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    // Construtores, getters e setters

    public DadosAutenticacao() {}

    public DadosAutenticacao(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
