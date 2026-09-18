package com.gestionbudget.dto;

public class CategorieMontantDTO {
    private Long id;
    private String name;
    private Double montant;

    public CategorieMontantDTO(Long id, String name, Double montant) {
        this.id = id;
        this.name = name;
        this.montant = montant;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}