package br.com.forumhub.ForumHub.model.entities;

import br.com.forumhub.ForumHub.dto.usuario.DadosCadastroUsuario;
import br.com.forumhub.ForumHub.dto.usuario.DadosUsuarioAtualizacao;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/** Representa um usuário no sistema de fórum.
 * Um usuário pode ter tópicos e respostas associados a ele.*/

@Table(name = "usuarios")
@Entity(name = "Usuario")
@EqualsAndHashCode(of = "id") // Gera equals e hashCode com base no id
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Topico> topicos = new ArrayList<>();

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Resposta> respostas = new ArrayList<>();

    private Boolean ativo;

    /**
     * Construtor para inicializar um novo usuário com os dados fornecidos.
     *
     * @param nome Nome do usuário.
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     */
    public Usuario(String nome, String email, String senha) {
        this.id = null;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.ativo = true;
    }

    public Usuario() {
    }

    /**
     * Construtor para instanciar um usuário com dados completos.
     *
     * @param id ID do usuário.
     * @param nome Nome do usuário.
     * @param email Email do usuário.
     * @param senha Senha do usuário.
     * @param topicos Lista de tópicos associados ao usuário.
     * @param respostas Lista de respostas associadas ao usuário.
     * @param ativo Status de atividade do usuário.
     */
    public Usuario(Long id, String nome, String email, String senha, List<Topico> topicos, List<Resposta> respostas, Boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.topicos = topicos;
        this.respostas = respostas;
        this.ativo = ativo;
    }

    /**
     * Construtor para criar um usuário a partir de dados de cadastro.
     *
     * @param cadastro Dados de cadastro do usuário.
     */
    public Usuario(DadosCadastroUsuario cadastro) {
        this.id = null;
        this.nome = cadastro.nome();
        this.email = cadastro.email();
        this.senha = cadastro.senha();
        this.ativo = true;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public List<Topico> getTopicos() {
        return topicos;
    }

    public List<Resposta> getRespostas() {
        return respostas;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Exemplo com papel único "ROLE_USER". Expanda para múltiplos papéis, se necessário.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Atualiza os dados do usuário com os dados fornecidos.
     *
     * @param dados Dados de atualização do usuário.
     */
    public void atualizar(DadosUsuarioAtualizacao dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }

        if (dados.senha() != null) {
            this.senha = dados.senha();
        }
    }

    /**
     * Marca o usuário como inativo, efetivamente deletando-o do sistema.
     */
    public void deletar() {
        this.ativo = false;
    }
}