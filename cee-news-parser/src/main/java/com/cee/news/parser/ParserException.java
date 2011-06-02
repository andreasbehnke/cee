package com.cee.news.parser;

public class ParserException extends Exception {

    private static final long serialVersionUID = 1981475052956331372L;

    public ParserException() {}

    public ParserException(String message) {
        super(message);
    }

    public ParserException(Throwable cause) {
        super(cause);
    }

    public ParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
