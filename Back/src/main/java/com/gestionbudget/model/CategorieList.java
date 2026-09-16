package com.gestionbudget.model;

import jakarta.persistence.*;
import lombok.*;

@Table (name="categorie_list")
@Entity
@Getter
@Setter
public class CategorieList {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name="name")
    private String name;
}
