import { useState, useEffect, type FormEvent } from "react";
import { verifierBudgetSuffisant } from ".././services/PetiteFonctionnalite";

function soummetreBudget(
    budgetTotal: number, 
    setErreur: (erreur: string) => void, 
    rechargerEtatBudget: () => Promise<void>
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

            // Au lieu de faire confiance à la réponse du POST (dont la forme des
            // catégories diffère de celle du GET, sans le champ "montant"), on
            // recharge l'état complet et à jour depuis la même source de vérité
            // que celle utilisée au montage de la page.
            await rechargerEtatBudget();
        } catch (error) {
            setErreur("Erreur lors de la connexion au serveur");
        }
    };
    return genererSoummission;
}

function soummetreBudgetCategorie(
    budgetAffiche: number,
    budgetcategorie: number[], 
    setErreur: (erreur: string) => void,
    setBlocker: (blocker: boolean) => void,
    blocker: boolean,
    rechargerEtatBudget: () => Promise<void>
) {
    const genererSoummissionCategorie = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setErreur("");
        console.log("Budget par catégorie à soumettre :", budgetcategorie);
        verifierBudgetSuffisant(budgetAffiche, budgetcategorie, setErreur, setBlocker);
        if (!blocker) {
            try {
                const userID = localStorage.getItem("userID");
                const response = await fetch("http://localhost:8081/api/budget/budgetCategorie", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ userId: Number(userID), montant: budgetcategorie }),
                });

                if (!response.ok) {
                    setErreur("Erreur lors de la soumission des budgets par catégorie");
                    return;
                }

                // Même principe : on recharge l'état réel depuis le backend plutôt
                // que de rester sur l'état local optimiste.
                await rechargerEtatBudget();
            } catch (error) {
                setErreur("Erreur lors de la connexion au serveur");
            }
        }
    };
    return genererSoummissionCategorie;
}

export default function Home() {
    const [budgetTotal, setBudgetTotal] = useState<number>(0);
    const [erreur, setErreur] = useState<string>("");
    const [Affsuite, setAffsuite] = useState<boolean>(false);
    const [budgetAffiche, setBudgetAffiche] = useState<number>(0);
    const [categories, setCategories] = useState<any[]>([]);
    const [budgetcategorie, setBudgetCategorie] = useState<number[]>([]);
    const [blocker, setBlocker] = useState<boolean>(false);//ty iblokena ny input raha ilaina

    // Budget courant = Budget total (fixe, tel que saisi) - somme des budgets par
    // catégorie actuellement dans les inputs. C'est une valeur dérivée, recalculée
    // à chaque rendu (donc à chaque frappe de l'utilisateur), pas un état séparé :
    // ça garantit qu'elle est toujours synchronisée avec ce que l'utilisateur voit
    // à l'écran, sans risque de désynchronisation.
    const sommeCategories = budgetcategorie.reduce((acc, curr) => acc + (curr || 0), 0);
    const budgetCourant = budgetAffiche - sommeCategories;

    // Fonction unique de rechargement de l'état, réutilisée :
    //  - au montage de la page,
    //  - après la soumission du budget total,
    //  - après la soumission des budgets par catégorie.
    // C'est la seule fonction qui écrit dans budgetAffiche / categories / budgetcategorie
    // à partir de données serveur, ce qui garantit une forme toujours cohérente.
    const chargerEtatBudget = async () => {
        const userID = localStorage.getItem("userID");
        if (!userID) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8081/api/budget/${userID}`);

            if (!response.ok) {
                setErreur("Impossible de récupérer l'état du budget");
                return;
            }

            const data = await response.json();
            const totalRecu = Number(data.budgetTotal) || 0;

            setBudgetAffiche(totalRecu);

            if (data.categories && data.categories.length > 0) {
                setCategories(data.categories);
                setBudgetCategorie(
                    data.categories.map((cat: any) => Number(cat.montant) || 0)
                );
            }

            if (totalRecu > 0) {
                setAffsuite(true);
            }
        } catch (error) {
            setErreur("Erreur lors de la connexion au serveur");
        }
    };

    useEffect(() => {
        chargerEtatBudget();
    }, []);

    // Garde-fou : si jamais categories change sans que budgetcategorie soit
    // encore aligné (cas limite), on complète les index manquants avec 0.
    useEffect(() => {
        setBudgetCategorie((prev) => {
            const next = [...prev];
            for (let i = 0; i < categories.length; i++) {
                if (next[i] === undefined) {
                    next[i] = 0;
                }
            }
            return next;
        });
    }, [categories]);

    return (
        <div className="home min-h-screen bg-black text-white flex flex-col items-center justify-center p-4">
            <div className="w-full max-w-md bg-neutral-900 border border-amber-500/30 rounded-xl shadow-2xl p-8 space-y-6">
                <h1 className="text-2xl font-bold text-center text-gold-hover">Bienvenue sur la page d'accueil</h1>

                <form onSubmit={soummetreBudget(budgetTotal, setErreur, chargerEtatBudget)} className="space-y-4">
                    <div>
                        <label htmlFor="budgetTotal" className="block text-sm font-medium text-gold-hover mb-1">
                            Saisir le budget (Ar):
                        </label>
                        <input 
                            type="number" 
                            placeholder="Saisir le budget" 
                            id="budgetTotal" 
                            value={budgetTotal || 0} 
                            onChange={(e) => setBudgetTotal(parseFloat(e.target.value) || 0)} 
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-black"
                        />
                    </div>

                    <div>
                        <button 
                            type="submit"
                            className="w-full bg-gold-hover hover:bg-gold hover:text-black text-white font-semibold py-2 px-4 rounded-lg transition duration-200"
                        >
                            Enregistrer
                        </button>
                    </div>
                </form>


                <div className="budget text-center space-y-2">
                    {budgetAffiche !== 0 && (
                        <p className="text-lg font-semibold text-amber-400 bg-neutral-800/80 py-2 px-4 rounded-lg border border-amber-500/20">
                            Budget total: {budgetAffiche.toLocaleString()} Ar
                        </p>
                    )}
                    {budgetAffiche !== 0 && (
                        <p
                            className={`text-lg font-semibold py-2 px-4 rounded-lg border ${
                                budgetCourant < 0
                                    ? "text-red-400 bg-red-950/40 border-red-500/30"
                                    : "text-emerald-400 bg-neutral-800/80 border-emerald-500/20"
                            }`}
                        >
                            Budget courant: {budgetCourant.toLocaleString()} Ar
                        </p>
                    )}
                </div>
                {(blocker || budgetCourant < 0) && (
                    <p className="erreur text-sm text-red-600 bg-red-50 p-3 rounded-lg border border-red-200 text-center">
                        Le budget total est insuffisant pour couvrir les budgets des catégories. Ajustez les montants par catégorie.
                    </p>
                )}

                {(Affsuite || budgetAffiche > 0) && categories.length > 0 && (
                    <form onSubmit={soummetreBudgetCategorie(budgetAffiche, budgetcategorie, setErreur, setBlocker, blocker, chargerEtatBudget)} className="space-y-4">
                        <h2 className="text-lg font-semibold text-center text-gold-hover">Budgets par catégorie</h2>

                        {categories.map((categorie, index) => (
                            <div key={categorie.id ?? `${categorie.name}-${index}`}>
                                <label htmlFor={`categorie-${categorie.id}`} className="block text-sm font-medium text-gold-hover mb-1">
                                    Budget pour {categorie.name} (Ar):
                                </label>
                                <input
                                    type="number"
                                    id={`categorie-${categorie.id}`}
                                    placeholder="Saisir le budget"
                                    value={budgetcategorie[index] || 0}
                                    onChange={(e) => {
                                        const montants = [...budgetcategorie];
                                        montants[index] = parseFloat(e.target.value) || 0;
                                        setBudgetCategorie(montants);
                                    }}
                                    required
                                    className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-black"
                                />
                            </div>
                        ))}

                        <button
                            type="submit"
                            disabled={budgetCourant < 0}
                            className="w-full bg-gold-hover hover:bg-gold hover:text-black text-white font-semibold py-2 px-4 rounded-lg transition duration-200 disabled:opacity-40 disabled:cursor-not-allowed disabled:hover:bg-gold-hover disabled:hover:text-white"
                        >
                            Enregistrer les budgets par catégorie
                        </button>
                    </form>
                )}

                <p className="text-center text-sm text-gray-400">
                    <a href="/deconnexion" className="text-red-400 hover:underline font-medium">
                        Se déconnecter
                    </a>
                </p>
            </div>
        </div>
    );
}