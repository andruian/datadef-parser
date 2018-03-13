package cz.melkamar.andruian.ddfparser.exception;

public class UnsupportedRdfFormatException extends RuntimeException {
    public UnsupportedRdfFormatException() {
    }

    public UnsupportedRdfFormatException(String message) {
        super(message);
    }

    public UnsupportedRdfFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnsupportedRdfFormatException(Throwable cause) {
        super(cause);
    }
}
