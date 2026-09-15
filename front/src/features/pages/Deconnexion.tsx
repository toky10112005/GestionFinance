import { Navigate } from 'react-router-dom';

export default function Deconnexion() {
    localStorage.clear();
    return <Navigate to="/" />;
}