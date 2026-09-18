export async function verifierBudgetSuffisant(
    budgetAffiche: number,
    budgetCategorie: number[],
    setErreur: (erreur: string) => void,
    setBlocker: (blocker: boolean) => void
) {
    const BudgetCategorieTotal = budgetCategorie.reduce((acc, curr) => acc + curr, 0);

    if (BudgetCategorieTotal > budgetAffiche) {
        setErreur("Le budget total est insuffisant.");
        // On ne modifie plus budgetAffiche ici : le "Budget total" doit rester
        // la valeur saisie au départ. Le calcul du reste (budget courant) est
        // désormais géré côté Home.tsx, en affichage dérivé.
        setBlocker(true);
    } else {
        setBlocker(false);
        setErreur("");
    }
}