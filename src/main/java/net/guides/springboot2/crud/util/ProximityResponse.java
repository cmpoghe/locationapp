package net.guides.springboot2.crud.util;

public class ProximityResponse {
    private String message;

    public ProximityResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

