package com.javanauta.usuario.infrastrcture.exceptions;

public class ConflictExceptions extends RuntimeException {
    public ConflictExceptions(String message) {
        super(message);
    }
    public ConflictExceptions(String message, Throwable throwable){super(message, throwable);}
}
