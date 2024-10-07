package com.instagram.api.exceptions;

import java.time.*;

public class ErrorDetails {
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ErrorDetails(){

    }

    public ErrorDetails(String message, String details, LocalDateTime timestamp) {
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    
}
