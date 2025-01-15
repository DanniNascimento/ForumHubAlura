package br.com.forumhub.ForumHub.infra.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Esta classe centraliza o tratamento de exceções na aplicação.
 * Ela usa a anotação `@RestControllerAdvice`, o que significa que ela
 * captura exceções em todo o sistema e envia respostas apropriadas.
 */
@RestControllerAdvice
public class TratamentoDeErros {

    /**
     * Método que trata exceções do tipo `EntityNotFoundException`.
     * Esse tipo de erro é geralmente lançado quando tentamos acessar
     * um recurso no banco de dados que não existe (por exemplo, ao tentar
     * buscar um usuário ou tópico que não está registrado).
     *
     * @return Resposta HTTP 404 (Not Found) para indicar que o recurso não foi encontrado.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> tratarErro404() {
        // Retorna uma resposta HTTP 404 indicando que o recurso não foi encontrado
        return ResponseEntity.notFound().build();
    }

    /**
     * Método que trata exceções do tipo `MethodArgumentNotValidException`.
     * Esse tipo de erro ocorre quando há falha na validação de dados fornecidos
     * pelo cliente (por exemplo, ao enviar dados inválidos ou incompletos em um formulário).
     *
     * @param ex Exceção que contém detalhes sobre os erros de validação.
     * @return Resposta HTTP 400 (Bad Request) contendo detalhes dos erros de validação.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> tratarErro400(MethodArgumentNotValidException ex) {
        // Obtém os erros de validação dos campos (como nome do campo e mensagem de erro)
        var erros = ex.getFieldErrors();

        // Retorna uma resposta 400 com os erros de validação formatados
        return ResponseEntity.badRequest()
                .body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    /**
     * Método que trata exceções do tipo `ValidacaoException`.
     * Essa exceção é lançada quando há falha em alguma regra de negócio
     * personalizada, como validação de dados que precisam seguir uma regra
     * específica além da validação padrão (ex: "o título do tópico não pode ser vazio").
     *
     * @param ex Exceção contendo a mensagem de erro relacionada à regra de negócio.
     * @return Resposta HTTP 400 (Bad Request) com a mensagem de erro.
     */
    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<?> tratarErroValidacao(ValidacaoException ex) {
        // Retorna uma resposta 400 com a mensagem de erro da exceção
        return ResponseEntity.badRequest()
                .body(new DadosValidacaoException(ex));
    }

    /**
     * Classe interna para encapsular os detalhes de um erro de validação de campo.
     * Ela ajuda a formatar a resposta de erro de maneira compreensível para o cliente.
     */
    private record DadosErroValidacao(String campo, String mensagem) {
        /**
         * Construtor que recebe um objeto `FieldError` (que contém o erro de validação de um campo)
         * e cria um objeto `DadosErroValidacao`, com o nome do campo e a mensagem de erro.
         *
         * @param error Objeto `FieldError` que contém o erro de validação de um campo.
         */
        public DadosErroValidacao(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    /**
     * Classe interna para encapsular a mensagem de erro de uma exceção de validação customizada (`ValidacaoException`).
     */
    private record DadosValidacaoException(String mensagem) {
        /**
         * Construtor que converte uma exceção de validação customizada (`ValidacaoException`)
         * em um objeto `DadosValidacaoException`, que apenas contém a mensagem de erro.
         *
         * @param ex Exceção de validação personalizada que contém a mensagem de erro.
         */
        public DadosValidacaoException(ValidacaoException ex) {
            this(ex.getMessage());
        }
    }
}
