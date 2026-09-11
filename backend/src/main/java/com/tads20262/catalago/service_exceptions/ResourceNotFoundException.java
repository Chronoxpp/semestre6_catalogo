package com.tads20262.catalago.service_exceptions;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException (String msg)
    {
        super(msg);
    }
}
