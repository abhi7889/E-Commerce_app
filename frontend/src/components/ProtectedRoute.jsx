import React from 'react';
import { Navigate } from 'react-router-dom';
import AuthService from '../services/AuthService';

export default function ProtectedRoute({ children }) {
  return AuthService.isAuthenticated() ? children : <Navigate to="/login" replace />;
}