package com.example.taskmanagersystem.exceptions;

public class UnauthorizedErrorException extends IllegalArgumentException {
    public UnauthorizedErrorException(String msg) {
        super(msg);
    }
}
