import { useState, type FormEvent } from "react";
import { useNavigate, Link } from "react-router-dom";

interface Client {
    username: string;
    password: string;
    email: string;
    role: string;
}

export default function Signin() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [email, setEmail] = useState("");
    const [erreur, setErreur] = useState("");

    const navigate = useNavigate();

    const handleSubmission = async (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        try {
            const response = await fetch("http://localhost:8081/api/auth/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ username, password, email, role: "USER" } as Client),
            });

            if (!response.ok) {
                setErreur("Erreur lors de l'inscription: username déjà utilisé");
                throw new Error("Erreur lors de la soumission du formulaire");
            }
            
            setErreur("");
            navigate("/");

        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="signin min-h-screen bg-black flex flex-col justify-center items-center p-4">
            <div className="w-full max-w-md bg-neutral-900 border border-amber-500/30 rounded-xl shadow-2xl p-8 space-y-6">
                <h1 className="text-2xl font-bold text-center text-gold-hover">Veuillez vous inscrire</h1>
                
                <form onSubmit={handleSubmission} className="space-y-4">
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
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-black"
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gold-hover mb-1">
                            Email:
                        </label>
                        <input 
                            type="email" 
                            value={email} 
                            onChange={(e) => setEmail(e.target.value)} 
                            placeholder="exemple@domaine.com" 
                            required 
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-black"
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
                            className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:outline-none text-black"
                        />
                    </div>

                    <div>
                        <button 
                            type="submit"
                            className="w-full bg-gold-hover hover:bg-gold hover:text-black text-white font-semibold py-2 px-4 rounded-lg transition duration-200"
                        >
                            S'inscrire
                        </button>
                    </div>

                    {erreur !== "" && (
                        <p className="error text-sm text-red-600 bg-red-50 p-3 rounded-lg border border-red-200 text-center">
                            {erreur}
                        </p>
                    )}
                </form>

                <p className="text-center text-sm text-gray-400">
                    Déjà un compte ?{" "}
                    <Link to="/" className="text-blue-500 hover:underline font-medium">
                        Connectez-vous ici
                    </Link>
                </p>
            </div>
        </div>
    );
}