import { useState, type SubmitEvent } from "react";
import { useNavigate } from 'react-router-dom';

function soumission(username: string, password: string, setErreur: (erreur: string) => void) {
    const navigate = useNavigate();
    const genererSoumission= async (event: SubmitEvent<HTMLFormElement>)=>{
        event.preventDefault();
        try{
            const response= await fetch("http://localhost:8081/api/auth/login", {
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body: JSON.stringify({ username, password }),
            });

            if(!response.ok){
                setErreur("Erreur lors de la connexion: username ou mot de passe incorrect");
                throw new Error("Erreur lors de la soumission du formulaire");
            }
            const data= await response.json();

             if (data.token) {
                localStorage.setItem("token", data.token);
            }
            
            if(data.userID){
                localStorage.setItem("userID", data.userID);
            }
            
            if(data.budgetTotal){
                localStorage.setItem("budgetTotal", data.budgetTotal);
            }
            console.log("budget dans le compte:", data.budgetTotal);
            console.log("userID:", data.userID);
            navigate("/home");
            setErreur("");

        }catch(error){
            console.error(error);
        }
    };
    return genererSoumission;
}

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [erreur, setErreur] = useState("");

return (
        <div className="login min-h-screen bg-black flex flex-col justify-center items-center p-4">
            <div className="w-full max-w-md bg-neutral-900 border border-amber-500/30 rounded-xl shadow-2xl p-8 space-y-6">
                <h1 className="text-2xl font-bold text-center text-gold-hover">Veuillez vous connecter</h1>
                
                <form onSubmit={soumission(username, password, setErreur)} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gold-hover mb-1">
                            Nom d'utilisateur:
                        </label>
                        <input 
                            type="text" 
                            value={username} 
                            onChange={(e) => setUsername(e.target.value)} 
                            placeholder="vody" 
                            required 
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gold-hover mb-1">
                            Mot de passe:
                        </label>
                        <input 
                            type="password" 
                            value={password} 
                            onChange={(e) => setPassword(e.target.value)} 
                            placeholder="vody" 
                            required 
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none"
                        />
                    </div>

                    <div>
                        <button 
                            type="submit"
                            className="w-full bg-gold-hover hover:bg-gold hover:text-black text-white font-semibold py-2 px-4 rounded-lg transition duration-200"
                        >
                            Se connecter
                        </button>
                    </div>

                    {erreur !== "" && (
                        <p className="error text-sm text-red-600 bg-red-50 p-3 rounded-lg border border-red-200 text-center">
                            {erreur}
                        </p>
                    )}
                </form>

                <p className="text-center text-sm text-gray-600">
                    Pas encore inscrit ?{" "}
                    <a href="/signin" className="text-blue-600 hover:underline font-medium">
                        Inscrivez-vous ici
                    </a>
                </p>
            </div>
        </div>
    );
} 