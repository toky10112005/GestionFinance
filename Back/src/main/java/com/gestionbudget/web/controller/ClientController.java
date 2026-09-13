package com.gestionbudget.web.controller;

import org.springframework.web.bind.annotation.*;

import com.gestionbudget.model.Clients;
import com.gestionbudget.model.SoldeCompteCourant;
import com.gestionbudget.service.ClientService;

import java.util.List;

import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/clients")
@CrossOrigin(origins = "http://localhost:5173") //Important ,autorise React Vite
public class ClientController {
    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public List<Clients> getAllClients() {
        return clientService.getAllClients();
    }

    @GetMapping("/solde-compte-courant")
    public Page<SoldeCompteCourant> getAllSoldeCompteCourant(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return clientService.getAllSoldeCompteCourant(page,size);
    }
}
