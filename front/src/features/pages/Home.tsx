import { useState, useEffect, type FormEvent } from "react";

function soummetreBudget(
    budgetTotal: number, 
    setErreur: (erreur: string) => void, 
    setAffsuite: (affsuite: boolean) => void, 
    setBudgetAffiche: (budget: number) => void, 
    setCategories: (categories: any[]) => void
) {
    const genererSoummission = async (event: FormEvent<HTMLFormElement>) => {
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
                // Sauvegarde pour conserver l'affichage après un rafraîchissement
                localStorage.setItem("categories", JSON.stringify(data.categories));
            }
           
        } catch (error) {
            setErreur("Erreur lors de la connexion au serveur");
        }
    };
    return genererSoummission;
}

export default function Home() {
    const [budgetTotal, setBudgetTotal] = useState<number>(0);
    const [erreur, setErreur] = useState<string>("");
    const [Affsuite, setAffsuite] = useState<boolean>(false);
    const [budgetAffiche, setBudgetAffiche] = useState<number>(0);
    const [categories, setCategories] = useState<any[]>([]);

    useEffect(() => {
        const localBudget = localStorage.getItem("budgetTotal");
        const localCategories = localStorage.getItem("categories");

        if (localBudget) {
            setBudgetAffiche(parseFloat(localBudget));
        }

        if (localCategories) {
            setCategories(JSON.parse(localCategories));
        }
    }, []);

    return (
        <div className="home">
            <h1>Bienvenue sur la page d'accueil:</h1>

            <form onSubmit={soummetreBudget(budgetTotal, setErreur, setAffsuite, setBudgetAffiche, setCategories)}>
                <label htmlFor="budgetTotal">Saisir le budget(Ar)</label>
                <input 
                    type="number" 
                    placeholder="Saisir le budget" 
                    id="budgetTotal" 
                    value={budgetTotal || 0} 
                    onChange={(e) => setBudgetTotal(parseFloat(e.target.value) || 0)} 
                />
                <button type="submit">Enregistrer</button>
            </form>

            {erreur && <p className="erreur">{erreur}</p>}

            <div className="budget">
                 {/* Formatage pour l'affichage numérique uniquement */}
                 {budgetAffiche !== 0 && <p>Budget total: {budgetAffiche.toLocaleString()} Ar</p>}
            </div>

            <p><a href="/deconnexion">Se déconnecter</a></p>

            {/* Affiche le bloc si budgetAffiche est supérieur à 0 et qu'il y a des catégories */}
            {(Affsuite || budgetAffiche > 0) && categories.length > 0 && (
                <form>
                    <ul>
                        {categories.map((categorie) => (
                            <li key={categorie.id}>{categorie.name}</li>
                        ))}
                    </ul>        
                </form>
            )}
        </div>
    );
}