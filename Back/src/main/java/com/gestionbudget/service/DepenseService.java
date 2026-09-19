package com.gestionbudget.service;

import org.springframework.stereotype.Service;
import com.gestionbudget.model.Depense;
import com.gestionbudget.repository.DepenseRepository;
import java.util.List;

@Service
public class DepenseService {
    private final DepenseRepository depenseRepository;

    public DepenseService(DepenseRepository depenseRepository) {
        this.depenseRepository = depenseRepository;
    }

    public Depense saveDepense(Depense depense) {
        return depenseRepository.save(depense);
    }

    public List<Depense> findByBudgetCategorieId(Long budgetCategorieId) {
        return depenseRepository.findByBudgetCategorieId(budgetCategorieId);
    }

    // Total déjà dépensé pour un BudgetCategorie donné (0.0 si aucune dépense).
    public Double getTotalDepensesPourCategorie(Long budgetCategorieId) {
        Double total = depenseRepository.sumMontantByBudgetCategorieId(budgetCategorieId);
        return total != null ? total : 0.0;
    }
}