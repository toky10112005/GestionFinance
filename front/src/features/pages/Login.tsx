import { useState, type SubmitEvent } from "react";


function soumission(username: string, password: string) {
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
                throw new Error("Erreur lors de la soumission du formulaire");
            }
            const data= await response.json();
            console.log("Réponse du serveur:", data);

             if (data.token) {
                localStorage.setItem("token", data.token);
            }

        }catch(error){
            console.error("Erreur lors de la soumission du formulaire:", error);
        }
    };
    return genererSoumission;
}

export default function Login(){
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    return (
        <div className="login">
            <h1>Veuiller vous connecter</h1>
            <form onSubmit={soumission(username, password)}>
                <p>Nom d'utilisateur: 
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)} placeholder="vody" /></p>
                <p>Mot de passe:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="vody" /></p>
                <p><button type="submit">Se connecter</button></p>
            </form>
        </div>
    );
} 