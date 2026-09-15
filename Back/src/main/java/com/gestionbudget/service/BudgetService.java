package com.gestionbudget.service;

import org.springframework.stereotype.Service;

import com.gestionbudget.model.Budget;
import com.gestionbudget.repository.BudgetRepository;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepo;

    public BudgetService(BudgetRepository budgetRepo) {
        this.budgetRepo = budgetRepo;
    }

    public Budget saveBudget(Budget budget) {
        return budgetRepo.save(budget);
    }
    
}
