package com.gestionbudget.service;

import com.gestionbudget.model.BudgetCategorie;
import com.gestionbudget.repository.BudgetCategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BudgetCategorieService {
    private final BudgetCategorieRepository budgetCategorieRepository;

    public BudgetCategorieService(BudgetCategorieRepository budgetCategorieRepository) {
        this.budgetCategorieRepository = budgetCategorieRepository;
    }

    public BudgetCategorie saveBudgetCategorie(BudgetCategorie budgetCategorie) {
        return budgetCategorieRepository.save(budgetCategorie);
    }

    public List<BudgetCategorie> getAllBudgetCategories() {
        return budgetCategorieRepository.findAll();
    }

    public BudgetCategorie getBudgetCategorieById(Long id) {
        return budgetCategorieRepository.findById(id).orElse(null);
    }

    // Nouvelle méthode : récupère les BudgetCategorie déjà enregistrées
    // pour un Budget donné (utilisée par le nouvel endpoint GET /api/budget/{userId}).
    public List<BudgetCategorie> getByBudgetId(Long budgetId) {
        return budgetCategorieRepository.findByBudget_Id(budgetId);
    }

    public BudgetCategorie updateBudgetCategorie(Long id, BudgetCategorie budgetCategorie) {
        BudgetCategorie existingBudgetCategorie = getBudgetCategorieById(id);

        if (existingBudgetCategorie == null) {
            return null;
        }

        existingBudgetCategorie.setBudget(budgetCategorie.getBudget());
        existingBudgetCategorie.setCategorieList(budgetCategorie.getCategorieList());

        return budgetCategorieRepository.save(existingBudgetCategorie);
    }

    public boolean deleteBudgetCategorie(Long id) {
        if (!budgetCategorieRepository.existsById(id)) {
            return false;
        }

        budgetCategorieRepository.deleteById(id);
        return true;
    }

    public BudgetCategorie findByBudgetIdCategorieId(Long budgetId, Long categorieId) {
        return budgetCategorieRepository.findByBudgetIdCategorieId(budgetId, categorieId);
    }
}