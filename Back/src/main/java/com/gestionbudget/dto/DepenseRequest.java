package com.gestionbudget.dto;

public class DepenseRequest {
    private Long CategorieId;
    private Long userId;
    private Double montant;

    public DepenseRequest() {
    }

    public DepenseRequest(Long budgetCategorieId, Long userId, Double montant) {
        this.CategorieId = budgetCategorieId;
        this.userId = userId;
        this.montant = montant;
    }

    public Long getCategorieId() {
        return CategorieId;
    }

    public void setCategorieId(Long categorieId) {
        this.CategorieId = categorieId;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}
