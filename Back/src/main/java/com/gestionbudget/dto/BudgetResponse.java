package com.gestionbudget.dto;

import com.gestionbudget.model.CategorieList;
import java.util.List;

public class BudgetResponse {
    private Long id;
    private Long userId;
    private Double budgetTotal;
    private List<CategorieList> categories;

    public BudgetResponse(Long id, Double budgetTotal,Long userId, List<CategorieList> categories) {
        this.id = id;
        this.userId = userId;
        this.budgetTotal = budgetTotal;
        this.categories = categories;
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
    public List<CategorieList> getCategories() {
        return categories;
    }
}
