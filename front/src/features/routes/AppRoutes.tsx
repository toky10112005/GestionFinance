import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import Signin from "../pages/Signin";
import Auth from "../filtres/Auth";
import Deconnexion from "../pages/Deconnexion";
import HistoriqueDepenses from "../pages/HistoriqueDepenses";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/signin" element={<Signin />} />
      <Route element={<Auth />}>
        <Route path="/home" element={<Home />} />
        <Route path="/historique-depenses/:idCategorie" element={<HistoriqueDepenses />} />
        <Route path="/deconnexion" element={<Deconnexion />} />
      </Route>
    </Routes>
  );
}