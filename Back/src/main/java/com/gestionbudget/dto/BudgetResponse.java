package com.gestionbudget.dto;

public class BudgetResponse {
    private Long id;
    private Long userId;
    private Double budgetTotal;

    public BudgetResponse(Long id, Double budgetTotal,Long userId ) {
        this.id = id;
        this.userId = userId;
        this.budgetTotal = budgetTotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
