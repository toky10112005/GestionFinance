package com.gestionbudget.service;

import org.springframework.stereotype.Service;

import com.gestionbudget.model.Clients;
import com.gestionbudget.model.SoldeCompteCourant;
import com.gestionbudget.repository.ClientRepository;
import com.gestionbudget.repository.SoldeCompteCourantRepository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@Service
public class ClientService {
    private final ClientRepository clientRepo;
    private final SoldeCompteCourantRepository soldeCompteCourantRepo;

    public ClientService(ClientRepository clientRepo, SoldeCompteCourantRepository soldeCompteCourantRepo) {
        this.clientRepo = clientRepo;
        this.soldeCompteCourantRepo = soldeCompteCourantRepo;
    }

    public List<Clients> getAllClients() {
        return clientRepo.findAll();
    }

    public Page<SoldeCompteCourant> getAllSoldeCompteCourant(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return soldeCompteCourantRepo.findAll(pageable);
    }
    
}
