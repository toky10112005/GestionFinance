import { useState, type SubmitEvent } from "react";

function soummetreBudget(budgetTotal: number, setErreur: (erreur: string) => void, setAffsuite: (affsuite: boolean) => void) {
    const genererSoummission =async (event:SubmitEvent<HTMLFormElement>)=>{
        event.preventDefault();
        try{
            const userID=localStorage.getItem("userID");
            const response=await fetch("http://localhost:8081/api/budget/budgetTotal", {
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                
                body: JSON.stringify({ userId: Number(userID),budgetTotal}),
            });
            if(!response.ok){
                setErreur("Erreur lors de la soumission du budget total");
            }
            else{
                setAffsuite(true);
            }
             const data=await response.json();
              console.log(data);
            //reponse de spring c'est une Liste des catégorie
           
        }catch(error){
            throw new Error("Erreur lors de la soumission du Budget total");
        }

    };
    return genererSoummission;
}


export default function Home() {
    const [budgetTotal, setBudgetTotal] = useState(0);
    const [erreur, setErreur] = useState("");
    const [Affsuite, setAffsuite] = useState(false);

    return (
        <div className="home">
            <h1>Bienvenue sur la page d'accueil:</h1>

            <form onSubmit={soummetreBudget(budgetTotal, setErreur, setAffsuite)}>
                <label htmlFor="budgetTotal">Saisir le budget(Ar)</label>
                <input type="number" placeholder="Saisir le budget" id="budgetTotal"value={budgetTotal} onChange={(e) => setBudgetTotal(parseFloat(e.target.value))} />
                <button type="submit">Enregistrer</button>
            </form>
            {erreur && <p className="erreur">{erreur}</p>}
            <div className="budget">
                 {localStorage.getItem("budgetTotal") && <p>Budget total: {localStorage.getItem("budgetTotal")} Ar</p>}
            </div>
            {/* {Affsuite && ( <form onSubmit={}>
                Affichena eto ny liste an ny categorie
                d asina input de buget tsirairay(izay tsy asina 
                valeur d 0 par defaut)
            </form>)} */}
           
            
        </div>
    );
}