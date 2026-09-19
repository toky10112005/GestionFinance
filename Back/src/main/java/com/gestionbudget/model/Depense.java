package com.gestionbudget.model;


import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
    
}
