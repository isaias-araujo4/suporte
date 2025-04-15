
import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getChamados } from '../services/api';
import Header from '../components/Header';
import TicketForm from '../components/TicketForm';
import TicketList from '../components/TicketList';
import TicketDetails from '../components/TicketDetails';
import '../styles/Dashboard.css';

function UserDashboard() {
  const { user } = useAuth();
  const [tickets, setTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showForm, setShowForm] = useState(false);
  const [selectedTicket, setSelectedTicket] = useState(null);

  useEffect(() => {
    fetchTickets();
  }, []);

  const fetchTickets = async () => {
    setLoading(true);
    try {
      const data = await getChamados(user.codigo);
      setTickets(data);
      setError('');
    } catch (err) {
      setError('Erro ao carregar chamados. Por favor, tente novamente.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleTicketCreated = (newTicket) => {
    setTickets([...tickets, newTicket]);
    setShowForm(false);
  };

  const handleTicketSelect = (ticket) => {
    setSelectedTicket(ticket);
  };

  return (
    <div className="dashboard">
      <Header title="Painel do Usuário" />
      <div className="container dashboard-content">
        {selectedTicket ? (
          <TicketDetails 
            ticket={selectedTicket} 
            onBack={() => setSelectedTicket(null)} 
          />
        ) : (
          <>
            <div className="dashboard-actions">
              <h2>Meus Chamados</h2>
              <button 
                onClick={() => setShowForm(!showForm)} 
                className="btn btn-primary"
              >
                {showForm ? 'Cancelar' : 'Criar Novo Chamado'}
              </button>
            </div>

            {error && <div className="error-message">{error}</div>}

            {showForm ? (
              <TicketForm 
                user={user} 
                onTicketCreated={handleTicketCreated} 
                onCancel={() => setShowForm(false)} 
              />
            ) : (
              <>
                {loading ? (
                  <div className="loading">Carregando chamados...</div>
                ) : (
                  <TicketList 
                    tickets={tickets} 
                    isUserView={true} 
                    onTicketSelect={handleTicketSelect} 
                    onRefresh={fetchTickets} 
                  />
                )}
              </>
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default UserDashboard;
