package com.gestionbudget.dto;

import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class BCategorie {
    private Long userId;
    private Double[] montant;
}
