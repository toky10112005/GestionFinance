package com.gestionbudget.dto;

public class LoginResponse {
    private String token;
    private String username;
    private Long userID;
    private Double budgetTotal;

    public LoginResponse(String token, String username,Long userID,Double budgetTotal) {
        this.token = token;
        this.username = username;
        this.userID = userID;
        this.budgetTotal = budgetTotal;
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
    public Double getBudgetTotal() {
        return budgetTotal;
    }
}