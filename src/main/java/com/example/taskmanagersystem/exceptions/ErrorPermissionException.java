package com.example.taskmanagersystem.exceptions;

public class ErrorPermissionException extends IllegalArgumentException {
    public ErrorPermissionException(String msg) {
        super(msg);
    }
}
