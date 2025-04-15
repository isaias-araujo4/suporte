
import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import '../styles/Header.css';

function Header({ title }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="header">
      <div className="container header-container">
        <h1>{title}</h1>
        <div className="user-info">
          <span>Olá, {user.nome} {user.sobrenome}</span>
          <button onClick={handleLogout} className="btn btn-secondary logout-btn">
            Sair
          </button>
        </div>
      </div>
    </header>
  );
}

export default Header;
