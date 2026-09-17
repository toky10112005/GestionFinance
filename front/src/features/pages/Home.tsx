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
        <div className="home min-h-screen bg-black text-white flex flex-col items-center justify-center p-4">
            <div className="w-full max-w-md bg-neutral-900 border border-amber-500/30 rounded-xl shadow-2xl p-8 space-y-6">
                <h1 className="text-2xl font-bold text-center text-gold-hover">Bienvenue sur la page d'accueil</h1>

                <form onSubmit={soummetreBudget(budgetTotal, setErreur, setAffsuite, setBudgetAffiche, setCategories)} className="space-y-4">
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

                {erreur && (
                    <p className="erreur text-sm text-red-600 bg-red-50 p-3 rounded-lg border border-red-200 text-center">
                        {erreur}
                    </p>
                )}

                <div className="budget text-center">
                    {budgetAffiche !== 0 && (
                        <p className="text-lg font-semibold text-amber-400 bg-neutral-800/80 py-2 px-4 rounded-lg border border-amber-500/20">
                            Budget total: {budgetAffiche.toLocaleString()} Ar
                        </p>
                    )}
                </div>

                {(Affsuite || budgetAffiche > 0) && categories.length > 0 && (
                    <form className="bg-neutral-800/50 p-4 rounded-lg border border-neutral-700">
                        <p className="text-sm font-medium text-amber-400 mb-2">Catégories :</p>
                        <ul className="space-y-2">
                            {categories.map((categorie) => (
                                <li key={categorie.id} className="bg-neutral-900 px-3 py-2 rounded-md border border-amber-500/10 text-sm text-gray-200">
                                    {categorie.name}
                                </li>
                            ))}
                        </ul>        
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