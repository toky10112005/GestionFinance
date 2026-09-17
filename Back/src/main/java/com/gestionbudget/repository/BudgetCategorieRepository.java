package com.gestionbudget.repository;

import com.gestionbudget.model.BudgetCategorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetCategorieRepository extends JpaRepository<BudgetCategorie, Long> {
}
