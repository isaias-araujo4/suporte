
import React, { useState } from 'react';
import { finalizarChamado } from '../services/api';
import '../styles/TicketDetails.css';

function TicketDetails({ ticket, technician, onTicketClosed, onBack }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return new Intl.DateTimeFormat('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    }).format(date);
  };

  const getCategoryLabel = (category) => {
    switch (category) {
      case 'REDE': return 'Rede';
      case 'HARDWARE': return 'Hardware';
      case 'SOFTWARE': return 'Software';
      default: return category;
    }
  };

  const handleCloseTicket = async () => {
    if (ticket.status === 'FINALIZADO') {
      return;
    }

    if (!window.confirm('Tem certeza que deseja finalizar este chamado?')) {
      return;
    }

    setLoading(true);
    setError('');

    try {
      const updatedTicket = await finalizarChamado(
        ticket.codigo,
        technician.codigo,
        `${technician.nome} ${technician.sobrenome}`
      );
      onTicketClosed(updatedTicket);
    } catch (err) {
      setError(err.message || 'Erro ao finalizar chamado');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="ticket-details">
      <div className="ticket-details-header">
        <button onClick={onBack} className="btn btn-secondary">
          Voltar
        </button>
        <h2>Detalhes do Chamado #{ticket.codigo}</h2>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="ticket-info">
        <div className="ticket-title">
          <h3>{ticket.titulo}</h3>
          <span className={`status-badge ${ticket.status === 'ABERTO' ? 'status-open' : 'status-closed'}`}>
            {ticket.status === 'ABERTO' ? 'Aberto' : 'Finalizado'}
          </span>
        </div>

        <div className="ticket-meta">
          <div className="meta-item">
            <span className="meta-label">Categoria:</span>
            <span className="meta-value">{getCategoryLabel(ticket.categoria)}</span>
          </div>

          <div className="meta-item">
            <span className="meta-label">Data de Criação:</span>
            <span className="meta-value">{formatDate(ticket.dataCriacao)}</span>
          </div>

          {ticket.dataFinalizacao && (
            <div className="meta-item">
              <span className="meta-label">Data de Finalização:</span>
              <span className="meta-value">{formatDate(ticket.dataFinalizacao)}</span>
            </div>
          )}
        </div>

        <div className="ticket-user">
          <span className="meta-label">Solicitante:</span>
          <span className="meta-value">{ticket.usuarioNome}</span>
        </div>

        {ticket.tecnicoNome && (
          <div className="ticket-technician">
            <span className="meta-label">Técnico Responsável:</span>
            <span className="meta-value">{ticket.tecnicoNome}</span>
          </div>
        )}

        <div className="ticket-description">
          <h4>Descrição:</h4>
          <p>{ticket.descricao}</p>
        </div>

        {technician && ticket.status === 'ABERTO' && (
          <div className="ticket-actions">
            <button
              onClick={handleCloseTicket}
              className="btn btn-success"
              disabled={loading}
            >
              {loading ? 'Finalizando...' : 'Finalizar Chamado'}
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default TicketDetails;