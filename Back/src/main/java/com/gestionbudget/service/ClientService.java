package com.gestionbudget.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gestionbudget.model.Client;
import com.gestionbudget.repository.ClientRepository;


@Service
public class ClientService {

   
    private PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;
    

    public ClientService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Client findByUsername(String username) {
        
        return clientRepository.findByUsername(username);
    }

    public void saveClient(Client client) {
        
        client.setPassword(passwordEncoder.encode(client.getPassword()));
        
        clientRepository.save(client);
    }

    public Client findByID(Long userId) {
        return clientRepository.findById(userId).orElse(null);
    }
    
}
