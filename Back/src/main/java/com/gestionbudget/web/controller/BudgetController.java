package com.gestionbudget.web.controller;

import com.gestionbudget.service.BudgetService;
import com.gestionbudget.model.Budget;
import com.gestionbudget.model.Client;
import com.gestionbudget.model.CategorieList;
import com.gestionbudget.dto.BudgetRequest;
import com.gestionbudget.dto.BudgetResponse;
import com.gestionbudget.service.ClientService;
import com.gestionbudget.service.CategorieListService;
import com.gestionbudget.dto.BCategorie;
import com.gestionbudget.model.BudgetCategorie;
import com.gestionbudget.service.BudgetCategorieService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final ClientService clientService;
    private final CategorieListService categorieListService;
    private final BudgetCategorieService budgetCategorieService;

    public BudgetController(BudgetService budgetService, ClientService clientService, CategorieListService categorieListService, BudgetCategorieService budgetCategorieService) {
        this.budgetService = budgetService;
        this.clientService = clientService;
        this.categorieListService = categorieListService;
        this.budgetCategorieService = budgetCategorieService;
    }

    @PostMapping("/budgetTotal")
    public ResponseEntity<?> saveBudgetTotal (@RequestBody BudgetRequest budgetRequest) {
        Budget budget = new Budget();
        Client client = clientService.findByID(budgetRequest.getUserId());
        budget.setClient(client);
        budget.setMontantTotal(budgetRequest.getBudgetTotal());
        budget.setMonth(LocalDate.now().getMonthValue());
        
        Budget confirmation= budgetService.findByClientId(budgetRequest.getUserId());
        Budget OK = null;

        List<CategorieList> categoriesList = categorieListService.getAllCategories();
        
        if(confirmation != null){
            confirmation.setMontantTotal(budgetRequest.getBudgetTotal());
            OK = budgetService.saveBudget(confirmation);
        } else {
           OK = budgetService.saveBudget(budget);
        }
        //Liste des catégorie:

       BudgetResponse budgetResponse = new BudgetResponse(OK.getId(), OK.getMontantTotal(), OK.getClient().getId(),categoriesList);
        return ResponseEntity.ok(budgetResponse);
    }

    @PostMapping("/budgetCategorie")
    public ResponseEntity<?> saveBudgetCategorie (@RequestBody BCategorie bCategorie) {
        Budget budget = budgetService.findByClientId(bCategorie.getUserId());
        if (budget == null) {
            return ResponseEntity.badRequest().body("Budget not found for userId: " + bCategorie.getUserId());
        }

        for (int i = 0; i < bCategorie.getMontant().length; i++) {
            BudgetCategorie budgetCategorie = new BudgetCategorie();
            budgetCategorie.setBudget(budget);
            CategorieList categorieList = categorieListService.getCategorieById((long) (i + 1));
            budgetCategorie.setCategorieList(categorieList);
            budgetCategorie.setMontant(bCategorie.getMontant()[i]);
            budgetCategorieService.saveBudgetCategorie(budgetCategorie);
        }
        return ResponseEntity.ok(true);
    }
}