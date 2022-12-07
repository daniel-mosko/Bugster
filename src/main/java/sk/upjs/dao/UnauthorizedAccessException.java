package sk.upjs.dao;

import java.io.Serial;

public class UnauthorizedAccessException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -3069912593572786581L;

    public UnauthorizedAccessException(String message) {
        super(message);
    }

}
