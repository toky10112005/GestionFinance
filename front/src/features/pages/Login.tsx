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
        <div className="login">
            <h1>Veuiller vous connecter</h1>
            <form onSubmit={soumission(username, password, setErreur)}>
                <p>Nom d'utilisateur: 
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="vody" required /></p>
                <p>Mot de passe:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="vody" required /></p>
                <p><button type="submit">Se connecter</button></p>
                {erreur!=="" && <p className="error">{erreur}</p>}
            </form>
        </div>
    );
} 