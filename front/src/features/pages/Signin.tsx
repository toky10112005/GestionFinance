import { useState, type SubmitEvent } from "react";
import { useNavigate } from 'react-router-dom';

interface Client {
    username: string;
    password: string;
    email: string;
    role: string;
}

function soumission(username: string, password: string, email: string, role: string, setErreur: (erreur: string) => void) {
    const navigate = useNavigate();
    const genererSoumission= async (event: SubmitEvent<HTMLFormElement>)=>{
        event.preventDefault();
        try{
            const response= await fetch("http://localhost:8081/api/auth/register", {
                method:"POST",
                headers:{
                    "Content-Type":"application/json"
                },
                body: JSON.stringify({ username, password, email, role } as Client),
            });

            if(!response.ok){
                setErreur("Erreur lors de l'inscription: username déjà utilisé");
                throw new Error("Erreur lors de la soumission du formulaire");
            }
            navigate("/");
            setErreur("");

        }catch(error){
            console.error(error);
        }
    };
    return genererSoumission;
}

export default function Signin(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [erreur, setErreur] = useState("");
    const [email, setEmail] = useState("");

    return (
        <div className="signin">
            <h1>Veuiller vous inscrire</h1>
            <form onSubmit={soumission(username, password, email, "user", setErreur)}>
                <p>Nom d'utilisateur: 
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="vody" required /></p>
                <p>Mot de passe:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="vody" required /></p>
                <p>Email:
                <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} placeholder="exemple@domaine.com" required /></p>
                <p><button type="submit">S'inscrire</button></p>
                {erreur!=="" && <p className="error">{erreur}</p>}
            </form>
        </div>
    );
} 