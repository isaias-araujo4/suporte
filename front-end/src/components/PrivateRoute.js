
import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function PrivateRoute({ children, userType }) {
  const { user, loading } = useAuth();

  if (loading) {
    return <div>Carregando...</div>;
  }

  if (!user) {
    return <Navigate to="/login" />;
  }

  if (userType && user.role !== userType) {
    return user.role === 'USUARIO' 
      ? <Navigate to="/usuario" /> 
      : <Navigate to="/tecnico" />;
  }

  return children;
}

export default PrivateRoute;
