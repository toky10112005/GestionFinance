package com.gestionbudget.dto;

// import com.gestionbudget.model.Client;

public class BudgetRequest {
    private Long userId;
    private Double budgetTotal;
    

    public BudgetRequest() {}

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(Double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }
}
