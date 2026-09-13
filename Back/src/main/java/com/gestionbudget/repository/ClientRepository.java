package com.gestionbudget.repository;

import org.springframework.stereotype.Repository;

import com.gestionbudget.model.Clients;

import org.springframework.data.jpa.repository.*;

// import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Clients, Long> {
    
}
