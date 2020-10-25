package ch.wba.accounting.banana.exception;

public class BananaReaderExceptions {
    public static class InvalidLineException extends RuntimeException {
        public InvalidLineException(String message) {
            super(message);
        }
    }

    public static class InvalidDateException extends RuntimeException {
        public InvalidDateException(String message) {
            super(message);
        }
    }
    public static class InvalidFieldNumberException extends RuntimeException {
        public InvalidFieldNumberException(String message) {
            super(message);
        }
    }
}
