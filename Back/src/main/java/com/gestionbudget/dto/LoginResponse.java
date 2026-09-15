package com.gestionbudget.dto;

public class LoginResponse {
    private String token;
    private String username;
    private Long userID;

    public LoginResponse(String token, String username,Long userID) {
        this.token = token;
        this.username = username;
        this.userID = userID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    } 
    public Long getUserID() {
        return userID;
    }
}