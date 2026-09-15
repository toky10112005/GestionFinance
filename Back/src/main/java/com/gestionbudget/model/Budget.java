package com.gestionbudget.model;

import jakarta.persistence.*;
import lombok.*;

@Table(name="budget")
@Entity
@Getter
@Setter
public class Budget {
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

   
    @ManyToOne 
    @JoinColumn (name="client_id", referencedColumnName = "id")
    private Client client;

    @Column(name="month")
    private int month;

    @Column(name="montant_total")
    private double montantTotal;

    
}
