
import React from 'react';
import '../styles/TicketList.css';

function TicketList({ tickets, isUserView, onTicketSelect, onRefresh }) {
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

  const getStatusClass = (status) => {
    return status === 'ABERTO' ? 'status-open' : 'status-closed';
  };

  const getCategoryLabel = (category) => {
    switch (category) {
      case 'REDE': return 'Rede';
      case 'HARDWARE': return 'Hardware';
      case 'SOFTWARE': return 'Software';
      default: return category;
    }
  };

  return (
    <div className="ticket-list-container">
      <div className="ticket-list-header">
        <h3>{isUserView ? 'Meus Chamados' : 'Todos os Chamados'}</h3>
        <button onClick={onRefresh} className="btn btn-secondary">
          Atualizar
        </button>
      </div>

      {tickets.length === 0 ? (
        <div className="no-tickets-message">
          {isUserView
            ? 'Você não possui chamados. Crie um novo chamado para começar.'
            : 'Não há chamados disponíveis no momento.'}
        </div>
      ) : (
        <div className="table-container">
          <table className="tickets-table">
            <thead>
              <tr>
                <th>codigo</th>
                <th>Título</th>
                <th>Categoria</th>
                <th>Status</th>
                <th>Data de Criação</th>
                {!isUserView && <th>Solicitante</th>}
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              {tickets.map((ticket) => (
                <tr key={ticket.codigo}>
                  <td>{ticket.codigo}</td>
                  <td>{ticket.titulo}</td>
                  <td>
                    <span className="category-badge">
                      {getCategoryLabel(ticket.categoria)}
                    </span>
                  </td>
                  <td>
                    <span className={`status-badge ${getStatusClass(ticket.status)}`}>
                      {ticket.status === 'ABERTO' ? 'Aberto' : 'Finalizado'}
                    </span>
                  </td>
                  <td>{formatDate(ticket.dataCriacao)}</td>
                  {!isUserView && <td>{ticket.usuarioNome}</td>}
                  <td>
                    <button
                      onClick={() => onTicketSelect(ticket)}
                      className="btn btn-primary btn-sm"
                    >
                      {isUserView ? 'Detalhes' : 'Gerenciar'}
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}

export default TicketList;