package com.gestionbudget.web.controller;

import com.gestionbudget.service.BudgetService;
import com.gestionbudget.model.Budget;
import com.gestionbudget.model.Client;
import com.gestionbudget.model.CategorieList;
import com.gestionbudget.dto.BudgetRequest;
import com.gestionbudget.dto.BudgetResponse;
import com.gestionbudget.dto.BudgetEtatResponse;
import com.gestionbudget.dto.CategorieMontantDTO;
import com.gestionbudget.service.ClientService;
import com.gestionbudget.service.CategorieListService;
import com.gestionbudget.dto.BCategorie;
import com.gestionbudget.model.BudgetCategorie;
import com.gestionbudget.service.BudgetCategorieService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    // Nouvel endpoint : renvoie l'état RÉEL du budget d'un utilisateur, tel qu'enregistré en base.
    // C'est ce que Home.tsx doit appeler au montage, à la place de localStorage,
    // pour que la liste des catégories et le budget total s'affichent correctement
    // dès la connexion, quel que soit l'état du navigateur (déconnexion, autre appareil, etc.).
    @GetMapping("/{userId}")
    public ResponseEntity<?> getBudgetEtat(@PathVariable Long userId) {
        Budget budget = budgetService.findByClientId(userId);
        List<CategorieList> categoriesList = categorieListService.getAllCategories();

        if (budget == null) {
            // Aucun budget créé pour l'instant : on renvoie quand même la liste
            // des catégories (montant à 0 partout) pour que le front sache
            // qu'il existe des catégories, sans budget total défini.
            List<CategorieMontantDTO> categoriesVides = categoriesList.stream()
                    .map(cat -> new CategorieMontantDTO(cat.getId(), cat.getName(), 0.0))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new BudgetEtatResponse(0.0, categoriesVides));
        }

        List<BudgetCategorie> budgetCategories = budgetCategorieService.getByBudgetId(budget.getId());

        // Table de correspondance catégorieId -> montant déjà enregistré
        Map<Long, Double> montantParCategorieId = budgetCategories.stream()
                .collect(Collectors.toMap(
                        bc -> bc.getCategorieList().getId(),
                        BudgetCategorie::getMontant,
                        (ancien, nouveau) -> nouveau
                ));

        List<CategorieMontantDTO> categoriesDTO = categoriesList.stream()
                .map(cat -> new CategorieMontantDTO(
                        cat.getId(),
                        cat.getName(),
                        montantParCategorieId.getOrDefault(cat.getId(), 0.0)
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(new BudgetEtatResponse(budget.getMontantTotal(), categoriesDTO));
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
            BudgetCategorie verification = budgetCategorieService.findByBudgetIdCategorieId(budget.getId(), categorieList.getId());
            if (verification != null) {
                verification.setMontant(bCategorie.getMontant()[i]);
                verification.setCreatedAt(LocalDate.now().toString());
                budgetCategorieService.saveBudgetCategorie(verification);
            }else{

                budgetCategorie.setCategorieList(categorieList);
                budgetCategorie.setMontant(bCategorie.getMontant()[i]);
                budgetCategorieService.saveBudgetCategorie(budgetCategorie);
            }

        }
        return ResponseEntity.ok(true);
    }
}