package com.gestionbudget.repository;

import org.springframework.stereotype.Repository;

import com.gestionbudget.model.Budget;

import org.springframework.data.jpa.repository.*;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    public Budget findByClientId(Long clientId);
}
