package com.gestionbudget.web.controller;

import com.gestionbudget.service.ClientService;
import com.gestionbudget.model.Client;
import com.gestionbudget.dto.LoginRequest;
import com.gestionbudget.dto.LoginResponse;
import com.gestionbudget.service.BudgetService;
import com.gestionbudget.model.Budget;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final private PasswordEncoder passwordEncoder;
    final private ClientService clientService;
    final private BudgetService budgetService;

    public AuthController(ClientService clientService, PasswordEncoder passwordEncoder, BudgetService budgetService) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.budgetService = budgetService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Client user = clientService.findByUsername(username);
       
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // TODO: générer un vrai JWT
            String fakeToken = "token-genere-exemple";
            Long userID =user.getId();

            Budget budgetTotal = budgetService.findByClientId(userID);
            //System.out.println("Budget total: " + budgetTotal.getMontantTotal());
           Double montantenvoyer = (budgetTotal != null && budgetTotal.getMontantTotal() != null) 
                        ? budgetTotal.getMontantTotal() 
                        : 0.0;
               
            LoginResponse response = new LoginResponse(fakeToken, username,userID,montantenvoyer);
            
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(401).body("Nom d'utilisateur ou mot de passe incorrect");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Client client) {
        if (clientService.findByUsername(client.getUsername()) != null) {
            return ResponseEntity.status(400).body("Nom d'utilisateur déjà utilisé");
        }
        clientService.saveClient(client);
        return ResponseEntity.ok("Client enregistré avec succès");
    }
}