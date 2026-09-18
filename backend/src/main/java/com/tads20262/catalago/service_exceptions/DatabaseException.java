package com.tads20262.catalago.service_exceptions;

public class DatabaseException extends RuntimeException
{
    public DatabaseException(String message) {
        super(message);
    }
}
