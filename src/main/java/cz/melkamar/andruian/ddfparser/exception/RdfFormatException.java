package cz.melkamar.andruian.ddfparser.exception;

public class RdfFormatException extends Exception {
    public RdfFormatException() {
    }

    public RdfFormatException(String message) {
        super(message);
    }

    public RdfFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public RdfFormatException(Throwable cause) {
        super(cause);
    }
}
