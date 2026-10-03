package com.edu.plataforma.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PerfilNoEncontradoException extends RuntimeException {
    public PerfilNoEncontradoException(String message) {
        super(message);
    }
}