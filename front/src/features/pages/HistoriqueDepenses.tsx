import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";

type DepenseHistorique = {
    categorie: string;
    budgetAlloue: number;
    montant: number;
    date: string | null;
};

export default function HistoriqueDepenses() {
    const { idCategorie } = useParams<{ idCategorie: string }>();
    const [depenses, setDepenses] = useState<DepenseHistorique[]>([]);
    const [erreur, setErreur] = useState("");
    const [chargement, setChargement] = useState(true);

    useEffect(() => {
        const userID = localStorage.getItem("userID");
        if (!userID || !idCategorie) {
            setErreur("Utilisateur ou catégorie introuvable");
            setChargement(false);
            return;
        }

        fetch(`http://localhost:8081/api/budget/depenses/${userID}/${idCategorie}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error("Impossible de récupérer l'historique");
                }
                return response.json();
            })
            .then((data: DepenseHistorique[]) => setDepenses(data))
            .catch(() => setErreur("Erreur lors de la récupération de l'historique"))
            .finally(() => setChargement(false));
    }, [idCategorie]);

    const budgetAlloue = depenses[0]?.budgetAlloue ?? 0;
    const categorie = depenses[0]?.categorie ?? "Catégorie";
    const totalDepense = depenses.reduce((total, depense) => total + depense.montant, 0);

    const afficherDate = (date: string | null) =>
        date ? new Date(date).toLocaleString() : "Date indisponible";

    return (
        <main className="min-h-screen bg-black text-white flex justify-center p-4">
            <section className="w-full max-w-2xl bg-neutral-900 border border-amber-500/30 rounded-xl shadow-2xl p-8 space-y-6 self-start mt-8">
                <div className="flex items-center justify-between gap-4">
                    <h1 className="text-2xl font-bold text-amber-400">Historique des dépenses</h1>
                    <Link to="/home" className="text-sm text-amber-400 hover:underline">Retour</Link>
                </div>

                {chargement && <p>Chargement...</p>}
                {erreur && <p className="text-red-400">{erreur}</p>}
                {!chargement && !erreur && (
                    <>
                        <div className="space-y-1 border-b border-neutral-700 pb-4">
                            <p>Catégorie : <strong>{categorie}</strong></p>
                            <p>Budget alloué : <strong>{budgetAlloue.toLocaleString()} Ar</strong></p>
                            <p>Total dépensé : <strong>{totalDepense.toLocaleString()} Ar</strong></p>
                        </div>
                        {depenses.length === 0 ? (
                            <p>Aucune dépense enregistrée dans cette catégorie.</p>
                        ) : (
                            <div className="space-y-2">
                                {depenses.map((depense, index) => (
                                    <div key={`${depense.date}-${index}`} className="flex justify-between gap-4 border-b border-neutral-800 py-2">
                                        <span>Dépense {index + 1}</span>
                                        <span>{depense.montant.toLocaleString()} Ar</span>
                                        <time dateTime={depense.date ?? undefined}>{afficherDate(depense.date)}</time>
                                    </div>
                                ))}
                            </div>
                        )}
                    </>
                )}
            </section>
        </main>
    );
}
