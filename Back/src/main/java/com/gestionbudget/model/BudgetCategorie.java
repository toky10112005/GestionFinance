package com.gestionbudget.model;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "budget_categorie")
public class BudgetCategorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "categorielist_id")
    private CategorieList categorieList;

    @Column(name = "montant")
    private Double montant;

    @Column (name = "created_at")
    private String createdAt;
}
