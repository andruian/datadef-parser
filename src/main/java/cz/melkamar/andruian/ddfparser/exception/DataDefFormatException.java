package cz.melkamar.andruian.ddfparser.exception;

public class DataDefFormatException extends Exception {
    public DataDefFormatException() {
    }

    public DataDefFormatException(String message) {
        super(message);
    }

    public DataDefFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataDefFormatException(Throwable cause) {
        super(cause);
    }
}
