package com.gestionbudget.service;

import org.springframework.stereotype.Service;

import com.gestionbudget.model.Client;
import com.gestionbudget.repository.ClientRepository;


// import java.util.List;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.PageRequest;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    
    }

    public Client findByUsername(String username) {
        return clientRepository.findByUsername(username);
    }
    
}
