package com.example.reactchesslive.ex;

public class GameMatcherTimeoutException extends RuntimeException {
    public GameMatcherTimeoutException(String message) {
        super(message);
    }

    public GameMatcherTimeoutException(Throwable cause) {
        super(cause);
    }
}
