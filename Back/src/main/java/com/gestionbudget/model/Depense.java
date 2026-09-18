package com.gestionbudget.model;


import lombok.*;
import jakarta.persistence.*;

@Table(name="depense")
@Entity 
@Setter
@Getter
public class Depense {
    @Id 
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "budget_categorie_id", nullable = false)
    private BudgetCategorie budgetCategorie;

    @Column(name = "montant", nullable = false)
    private Double montant;
    
}
