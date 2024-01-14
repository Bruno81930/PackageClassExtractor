package com.brunosottomayor;

public class ClassExtractorException extends Exception {
    // Default constructor
    public ClassExtractorException() {
        super();
    }

    // Constructor that accepts a message
    public ClassExtractorException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a Throwable cause
    public ClassExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor that accepts a Throwable cause
    public ClassExtractorException(Throwable cause) {
        super(cause);
    }
}
