package com.gestionbudget.dto;

import java.time.LocalDateTime;

public record DepenseHistoriqueResponse(
        String categorie,
        Double budgetAlloue,
        Double montant,
        LocalDateTime date
) {
}