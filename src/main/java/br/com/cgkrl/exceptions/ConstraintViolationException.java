package br.com.cgkrl.exceptions;

public class ConstraintViolationException extends Exception {
    public ConstraintViolationException(String message) {
        super(message);
    }
}
