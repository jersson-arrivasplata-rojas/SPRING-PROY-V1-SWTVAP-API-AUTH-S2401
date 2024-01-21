package com.jersson.arrivasplata.swtvap.api.auth.entity;

public class LogoutResponse {
    private int statusCode;

    // Constructor
    public LogoutResponse(int statusCode) {
        this.statusCode = statusCode;
    }

    // Getter
    public int getStatusCode() {
        return this.statusCode;
    }

    // Setter
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}