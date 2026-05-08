import { Navigate } from "react-router-dom";
import AuthService from "../../services/AuthService";

export default function PublicRoute({ children }) {
  return AuthService.isAuthenticated() ? (
    <Navigate to="/home" replace />
  ) : (
    children
  );
}
