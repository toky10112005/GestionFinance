package com.gestionbudget.dto;

public class CategorieMontantDTO {
    private Long id;
    private String name;
    private Double montantAlloue;
    private Double montantRestant;

    public CategorieMontantDTO(Long id, String name, Double montantAlloue, Double montantRestant) {
        this.id = id;
        this.name = name;
        this.montantAlloue = montantAlloue;
        this.montantRestant = montantRestant;
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

    public Double getMontantAlloue() {
        return montantAlloue;
    }

    public void setMontantAlloue(Double montantAlloue) {
        this.montantAlloue = montantAlloue;
    }

    public Double getMontantRestant() {
        return montantRestant;
    }

    public void setMontantRestant(Double montantRestant) {
        this.montantRestant = montantRestant;
    }
}