package com.gestionbudget.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

import javax.annotation.processing.Generated;

@Table(name = "clients")
@Entity
@Getter
@Setter
public class Clients {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "prenom", nullable = false)
    private String prenom;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    // @Column(name = "telephone", unique = true, nullable = false)
    // private String telephone;

    @Column(name = "date_naissance", nullable = false)
    private LocalDate date_naissance;
}
