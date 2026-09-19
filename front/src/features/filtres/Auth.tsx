import { Navigate, Outlet } from 'react-router-dom';

export default function Auth() {
    if (!localStorage.getItem("userID")) {
        return <Navigate to="/" />;
    }
    return <Outlet />;
}