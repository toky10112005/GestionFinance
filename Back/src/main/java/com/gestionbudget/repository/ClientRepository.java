package com.gestionbudget.repository;

import org.springframework.stereotype.Repository;

import com.gestionbudget.model.Client;

import org.springframework.data.jpa.repository.*;

// import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    public Client findByUsername(String username);
}
