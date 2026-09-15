package com.gestionbudget.web.controller;

import com.gestionbudget.service.BudgetService;
import com.gestionbudget.model.Budget;
import com.gestionbudget.model.Client;
import com.gestionbudget.dto.BudgetRequest;
import com.gestionbudget.dto.BudgetResponse;
import com.gestionbudget.service.ClientService;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final ClientService clientService;

    public BudgetController(BudgetService budgetService, ClientService clientService) {
        this.budgetService = budgetService;
        this.clientService = clientService;
    }

    @PostMapping("/budgetTotal")
    public ResponseEntity<?> saveBudgetTotal (@RequestBody BudgetRequest budgetRequest) {
        Budget budget = new Budget();
        Client client = clientService.findByID(budgetRequest.getUserId());
        budget.setClient(client);
        budget.setMontantTotal(budgetRequest.getBudgetTotal());
        //Ty mila maka an ilay valeur du mois courant..Mbola tsy mety Ito
        budget.setMonth(1);
        
        Budget confirmation= budgetService.findByClientId(budgetRequest.getUserId());
        Budget OK = null;
        if(confirmation != null){
            confirmation.setMontantTotal(budgetRequest.getBudgetTotal());
            OK = budgetService.saveBudget(confirmation);
        } else {
           OK = budgetService.saveBudget(budget);
        }
        // Budget OK=budgetService.saveBudget(budget);
       BudgetResponse budgetResponse = new BudgetResponse(OK.getId(), OK.getMontantTotal(), OK.getClient().getId());
        return ResponseEntity.ok(budgetResponse);
    }
}
