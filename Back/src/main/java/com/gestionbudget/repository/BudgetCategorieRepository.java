package com.gestionbudget.repository;

import com.gestionbudget.model.BudgetCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface BudgetCategorieRepository extends JpaRepository<BudgetCategorie, Long> {
    // Requête dérivée Spring Data : retrouve toutes les lignes BudgetCategorie
    // rattachées à un Budget donné, via son id (propriété "budget.id").
    List<BudgetCategorie> findByBudget_Id(Long budgetId);

    @Query("SELECT bc FROM BudgetCategorie bc WHERE bc.budget.id = :budgetId AND bc.categorieList.id = :categorieId")
    BudgetCategorie findByBudgetIdCategorieId(Long budgetId, Long categorieId);
}