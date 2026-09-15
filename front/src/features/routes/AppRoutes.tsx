import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import Signin from "../pages/Signin";
import Auth from "../filtres/Auth";
import Deconnexion from "../pages/Deconnexion";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/signin" element={<Signin />} />
      <Route element={<Auth />}>
        <Route path="/home" element={<Home />} />
        <Route path="/deconnexion" element={<Deconnexion />} />
      </Route>
    </Routes>
  );
}