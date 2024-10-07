package com.instagram.api.response;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class MessageResponse {

    private String message;

    public MessageResponse(String message){
        super();
        this.message = message;
    }

    // Getter và Setter nếu cần
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
