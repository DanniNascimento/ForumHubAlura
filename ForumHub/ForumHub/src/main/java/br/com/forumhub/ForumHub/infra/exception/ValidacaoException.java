package br.com.forumhub.ForumHub.infra.exception;

/**
 * Exceção personalizada para validação de regras de negócio.
 * Extende `RuntimeException` para indicar que é uma exceção não verificada.
 */
public class ValidacaoException extends RuntimeException {

    /**
     * Construtor que inicializa a exceção com uma mensagem específica.
     *
     * @param message mensagem descritiva do erro de validação.
     */
    public ValidacaoException(String message) {
        super(message);
    }
}
