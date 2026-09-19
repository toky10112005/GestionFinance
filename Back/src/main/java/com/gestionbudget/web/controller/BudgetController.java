package com.gestionbudget.web.controller;

import com.gestionbudget.service.BudgetService;
import com.gestionbudget.model.Budget;
import com.gestionbudget.model.Client;
import com.gestionbudget.model.CategorieList;
import com.gestionbudget.model.Depense;
import com.gestionbudget.dto.BudgetRequest;
import com.gestionbudget.dto.BudgetResponse;
import com.gestionbudget.dto.BudgetEtatResponse;
import com.gestionbudget.dto.CategorieMontantDTO;
import com.gestionbudget.dto.DepenseRequest;
import com.gestionbudget.dto.DepenseHistoriqueResponse;
import com.gestionbudget.service.ClientService;
import com.gestionbudget.service.CategorieListService;
import com.gestionbudget.service.DepenseService;
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
    private final DepenseService depenseService;

    public BudgetController(BudgetService budgetService, ClientService clientService, CategorieListService categorieListService, BudgetCategorieService budgetCategorieService, DepenseService depenseService) {
        this.budgetService = budgetService;
        this.clientService = clientService;
        this.categorieListService = categorieListService;
        this.budgetCategorieService = budgetCategorieService;
        this.depenseService = depenseService;
    }

    // Nouvel endpoint : renvoie l'état RÉEL du budget d'un utilisateur, tel qu'enregistré en base.
    // Pour chaque catégorie, on distingue désormais :
    //  - montantAlloue : ce que l'utilisateur a budgété (ne change jamais à cause d'une dépense)
    //  - montantRestant : montantAlloue - somme des dépenses déjà enregistrées pour cette catégorie
    @GetMapping("/{userId}")
    public ResponseEntity<?> getBudgetEtat(@PathVariable Long userId) {
        Budget budget = budgetService.findByClientId(userId);
        List<CategorieList> categoriesList = categorieListService.getAllCategories();

        if (budget == null) {
            // Aucun budget créé pour l'instant : on renvoie quand même la liste
            // des catégories (montants à 0 partout) pour que le front sache
            // qu'il existe des catégories, sans budget total défini.
            List<CategorieMontantDTO> categoriesVides = categoriesList.stream()
                    .map(cat -> new CategorieMontantDTO(cat.getId(), cat.getName(), 0.0, 0.0))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new BudgetEtatResponse(0.0, categoriesVides));
        }

        List<BudgetCategorie> budgetCategories = budgetCategorieService.getByBudgetId(budget.getId());

        // Table de correspondance catégorieId -> BudgetCategorie (pour retrouver
        // à la fois le montant alloué et l'id nécessaire au calcul des dépenses).
        Map<Long, BudgetCategorie> budgetCategorieParCategorieId = budgetCategories.stream()
                .collect(Collectors.toMap(
                        bc -> bc.getCategorieList().getId(),
                        bc -> bc,
                        (ancien, nouveau) -> nouveau
                ));

        List<CategorieMontantDTO> categoriesDTO = categoriesList.stream()
                .map(cat -> {
                    BudgetCategorie bc = budgetCategorieParCategorieId.get(cat.getId());
                    Double montantAlloue = bc != null ? bc.getMontant() : 0.0;
                    Double totalDepenses = bc != null ? depenseService.getTotalDepensesPourCategorie(bc.getId()) : 0.0;
                    Double montantRestant = montantAlloue - totalDepenses;
                    return new CategorieMontantDTO(cat.getId(), cat.getName(), montantAlloue, montantRestant);
                })
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

    @PostMapping ("/depense")
    public ResponseEntity<?> saveDepense(@RequestBody DepenseRequest depenseRequest) {
        Budget budget = budgetService.findByClientId(depenseRequest.getUserId());
        if (budget == null) {
            return ResponseEntity.badRequest().body("Budget not found for userId: " + depenseRequest.getUserId());
        }

        BudgetCategorie budgetCategorie = budgetCategorieService.findByBudgetIdCategorieId(budget.getId(), depenseRequest.getCategorieId());
        if (budgetCategorie == null) {
            return ResponseEntity.badRequest().body("BudgetCategorie not found for budgetId: " + budget.getId() + " and categorieId: " + depenseRequest.getCategorieId());
        }

        double montantDepense = depenseRequest.getMontant();
        double montantAlloue = budgetCategorie.getMontant();
        double totalDepensesExistantes = depenseService.getTotalDepensesPourCategorie(budgetCategorie.getId());
        double montantRestant = montantAlloue - totalDepensesExistantes;

        if (montantDepense > montantRestant) {
            return ResponseEntity.badRequest().body("Insufficient budget for this category. Montant restant: " + montantRestant);
        }

        // On ne modifie plus BudgetCategorie.montant : il reste l'allocation
        // d'origine, décidée par l'utilisateur. Seule la table "depense" garde
        // la trace des dépenses ; le montant restant est recalculé à la volée
        // (voir getBudgetEtat) en soustrayant la somme des dépenses de l'alloué.
        Depense depense = new Depense();
        depense.setBudgetCategorie(budgetCategorie);
        depense.setMontant(montantDepense);
        depenseService.saveDepense(depense);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/depenses/{userID}/{idCategorie}")
    public ResponseEntity<?> getDepenses(@PathVariable Long userID, @PathVariable Long idCategorie) {
        Budget budget = budgetService.findByClientId(userID);
        if (budget == null) {
            return ResponseEntity.badRequest().body("Budget not found for userId: " + userID);
        }

        BudgetCategorie budgetCategorie = budgetCategorieService.findByBudgetIdCategorieId(budget.getId(), idCategorie);
        if (budgetCategorie == null) {
            return ResponseEntity.badRequest().body("BudgetCategorie not found for budgetId: " + budget.getId() + " and categorieId: " + idCategorie);
        }

        List<DepenseHistoriqueResponse> historique = depenseService
                .findByBudgetCategorieId(budgetCategorie.getId())
                .stream()
                .map(depense -> new DepenseHistoriqueResponse(
                        budgetCategorie.getCategorieList().getName(),
                        budgetCategorie.getMontant(),
                        depense.getMontant(),
                        depense.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(historique);
    }
}