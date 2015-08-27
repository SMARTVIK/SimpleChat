package com.example.quickvik.chattingapp;

/**
 * Created by quickvik on 8/20/2015.
 */
public class Messages {

    public Messages(String message, String role, String timestamp) {
        this.message = message;
        this.role = role;
        this.timestamp = timestamp;
    }

    private String message;
    private String role;
    private String timestamp;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
