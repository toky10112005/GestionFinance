package com.gestionbudget.web.controller;

import com.gestionbudget.service.ClientService;
import com.gestionbudget.model.Client;
import com.gestionbudget.dto.LoginRequest;
import com.gestionbudget.dto.LoginResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final private ClientService clientService;

    public AuthController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Client user = clientService.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // TODO: générer un vrai JWT
            String fakeToken = "token-genere-exemple";
            LoginResponse response = new LoginResponse(fakeToken, username);
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