package com.gestionbudget.service;

import org.springframework.stereotype.Service;
import com.gestionbudget.model.Depense;
import com.gestionbudget.repository.DepenseRepository;

@Service
public class DepenseService {
    private final DepenseRepository depenseRepository;

    public DepenseService(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    public Depense saveDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    // Total déjà dépensé pour un BudgetCategorie donné (0.0 si aucune dépense).
    public Double getTotalDepensesPourCategorie(Long budgetCategorieId) {
        Double total = depenseRepository.sumMontantByBudgetCategorieId(budgetCategorieId);
        return total != null ? total : 0.0;
    }
}