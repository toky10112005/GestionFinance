package com.gestionbudget.dto;

import java.util.List;

public class BudgetEtatResponse {
    private Double budgetTotal;
    private List<CategorieMontantDTO> categories;

    public BudgetEtatResponse(Double budgetTotal, List<CategorieMontantDTO> categories) {
        this.budgetTotal = budgetTotal;
        this.categories = categories;
    }

    public Double getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(Double budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public List<CategorieMontantDTO> getCategories() {
        return categories;
    }

    public void setCategories(List<CategorieMontantDTO> categories) {
        this.categories = categories;
    }
}