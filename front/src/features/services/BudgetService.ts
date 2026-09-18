import type { FormEvent } from "react";

interface SoumettreParams {
    event: FormEvent<HTMLFormElement>;
    budgetTotal: number;
    setErreur: (erreur: string) => void;
    setAffsuite: (affsuite: boolean) => void;
    setBudgetAffiche: (budget: number) => void;
    setCategories: (categories: any[]) => void;
}

export async function soummetreBudget({
    event,
    budgetTotal,
    setErreur,
    setAffsuite,
    setBudgetAffiche,
    setCategories,
}: SoumettreParams) {
    event.preventDefault();
    setErreur("");

    try {
        const userID = localStorage.getItem("userID");
        const response = await fetch("http://localhost:8081/api/budget/budgetTotal", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ userId: Number(userID), budgetTotal }),
        });

        if (!response.ok) {
            setErreur("Erreur lors de la soumission du budget total");
            return;
        }

        const data = await response.json();

        if (data.budgetTotal !== undefined) {
            const valNum = Number(data.budgetTotal);
            setBudgetAffiche(valNum);
            localStorage.setItem("budgetTotal", valNum.toString());
        }

        if (data.categories && data.categories.length > 0) {
            setAffsuite(true);
            setCategories(data.categories);
            localStorage.setItem("categories", JSON.stringify(data.categories));
        }

    } catch (error) {
        setErreur("Erreur lors de la connexion au serveur");
    }
}