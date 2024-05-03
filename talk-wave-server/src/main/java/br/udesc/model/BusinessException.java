package br.udesc.model;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
