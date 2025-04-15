import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import { getChamados } from '../services/api';
import Header from '../components/Header';
import TicketList from '../components/TicketList';
import TicketDetails from '../components/TicketDetails';
import '../styles/Dashboard.css';

function TechnicianDashboard() {
  const { user } = useAuth();
  const [tickets, setTickets] = useState([]);
  const [filteredTickets, setFilteredTickets] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [selectedTicket, setSelectedTicket] = useState(null);

  // Filter states
  const [filterByCodigo, setFilterByCodigo] = useState('');
  const [filterByStatus, setFilterByStatus] = useState('');
  const [filterByCategory, setFilterByCategory] = useState('');

  useEffect(() => {
    fetchTickets();
  }, []);

  useEffect(() => {
    applyFilters();
  }, [tickets, filterByCodigo, filterByStatus, filterByCategory]);

  const fetchTickets = async () => {
    setLoading(true);
    try {
      const data = await getChamados();
      setTickets(data);
      setFilteredTickets(data);
      setError('');
    } catch (err) {
      setError('Erro ao carregar chamados. Por favor, tente novamente.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const applyFilters = () => {
    let result = [...tickets];

    if (filterByCodigo) {
      result = result.filter(ticket =>
        ticket.codigo.toString().includes(filterByCodigo)
      );
    }

    if (filterByStatus) {
      result = result.filter(ticket =>
        ticket.status === filterByStatus
      );
    }

    if (filterByCategory) {
      result = result.filter(ticket =>
        ticket.categoria === filterByCategory
      );
    }

    setFilteredTickets(result);
  };

  const handleTicketSelect = (ticket) => {
    setSelectedTicket(ticket);
  };

  const handleTicketClosed = (updatedTicket) => {
    const updatedTickets = tickets.map(ticket =>
      ticket.codigo === updatedTicket.codigo ? { ...ticket, ...updatedTicket } : ticket
    );
    setTickets(updatedTickets);
    setSelectedTicket({ ...selectedTicket, ...updatedTicket });
  };

  const clearFilters = () => {
    setFilterByCodigo('');
    setFilterByStatus('');
    setFilterByCategory('');
  };

  return (
    <div className="dashboard">
      <Header title="Painel do Técnico" />
      <div className="container dashboard-content">
        {selectedTicket ? (
          <TicketDetails
            ticket={selectedTicket}
            technician={user}
            onTicketClosed={handleTicketClosed}
            onBack={() => setSelectedTicket(null)}
          />
        ) : (
          <>
            <div className="filters-container">
              <h2>Filtrar Chamados</h2>
              <div className="filters-form">
                <div className="form-group">
                  <label htmlFor="filter-id">Codigo:</label>
                  <input
                    id="filter-id"
                    type="text"
                    className="form-control"
                    value={filterByCodigo}
                    onChange={(e) => setFilterByCodigo(e.target.value)}
                    placeholder="Filtrar por Código"
                  />
                </div>

                <div className="form-group">
                  <label htmlFor="filter-status">Status:</label>
                  <select
                    id="filter-status"
                    className="form-control"
                    value={filterByStatus}
                    onChange={(e) => setFilterByStatus(e.target.value)}
                  >
                    <option value="">Todos</option>
                    <option value="ABERTO">Aberto</option>
                    <option value="FINALIZADO">Finalizado</option>
                  </select>
                </div>

                <div className="form-group">
                  <label htmlFor="filter-category">Categoria:</label>
                  <select
                    id="filter-category"
                    className="form-control"
                    value={filterByCategory}
                    onChange={(e) => setFilterByCategory(e.target.value)}
                  >
                    <option value="">Todas</option>
                    <option value="REDE">Rede</option>
                    <option value="HARDWARE">Hardware</option>
                    <option value="SOFTWARE">Software</option>
                  </select>
                </div>

                <button onClick={clearFilters} className="btn btn-secondary">
                  Limpar Filtros
                </button>
              </div>
            </div>

            {error && <div className="error-message">{error}</div>}

            {loading ? (
              <div className="loading">Carregando chamados...</div>
            ) : (
              <TicketList
                tickets={filteredTickets}
                isUserView={false}
                onTicketSelect={handleTicketSelect}
                onRefresh={fetchTickets}
              />
            )}
          </>
        )}
      </div>
    </div>
  );
}

export default TechnicianDashboard;
