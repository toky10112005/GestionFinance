import { Routes, Route } from "react-router-dom";
import Login from "../pages/Login";
import Home from "../pages/Home";
import Signin from "../pages/Signin";

export default function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/home" element={<Home />} />
    </Routes>
  );
}