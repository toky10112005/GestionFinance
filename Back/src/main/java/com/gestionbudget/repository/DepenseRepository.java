package com.gestionbudget.repository;

import org.springframework.stereotype.Repository;
import com.gestionbudget.model.Depense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface DepenseRepository extends JpaRepository<Depense, Long> {
    List<Depense> findByBudgetCategorieId(Long budgetCategorieId);

    // Somme de toutes les dépenses enregistrées pour un BudgetCategorie donné.
    // COALESCE(..., 0) évite un résultat null quand aucune dépense n'existe encore.
    @Query("SELECT COALESCE(SUM(d.montant), 0) FROM Depense d WHERE d.budgetCategorie.id = :budgetCategorieId")
    Double sumMontantByBudgetCategorieId(@Param("budgetCategorieId") Long budgetCategorieId);
}