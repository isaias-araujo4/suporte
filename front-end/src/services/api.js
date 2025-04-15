import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

// Configuração do axios
const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para adicionar token de autenticação (se necessário no futuro)
api.interceptors.request.use(
  (config) => {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
      config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Funções de API

// Autenticação
export const login = async (email, senha) => {
  try {
    // Verifica se estamos em desenvolvimento e se devemos usar dados simulados
    if (process.env.NODE_ENV === 'development' && process.env.REACT_APP_USE_MOCK_DATA === 'true') {
      // Usuários de teste
      const users = [
        { id: 1, nome: 'João', sobrenome: 'Silva', email: 'usuario@teste.com', senha: '123456', role: 'USUARIO' },
        { id: 2, nome: 'Maria', sobrenome: 'Técnica', email: 'tecnico@teste.com', senha: '123456', role: 'TECNICO', cargo: 'Suporte Técnico' }
      ];
      
      const user = users.find(u => u.email === email && u.senha === senha);
      
      if (user) {
        const { senha, ...userData } = user;
        return userData;
      }
      
      throw new Error('Email ou senha inválidos');
    }
    
    // Chamada real à API
    const response = await api.post('/login', { email, senha });
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || 'Erro ao fazer login');
    }
    throw error;
  }
};

// Chamados
export const getChamados = async (usuarioCodigo = null) => {
  try {
    // Verifica se estamos em desenvolvimento e se devemos usar dados simulados
    if (process.env.NODE_ENV === 'development' && process.env.REACT_APP_USE_MOCK_DATA === 'true') {
      const chamados = [
        {
          id: 1,
          titulo: 'Problema com internet',
          descricao: 'Não consigo acessar a internet no meu computador.',
          status: 'ABERTO',
          dataCriacao: '2023-05-10T14:30:00',
          dataFinalizacao: null,
          categoria: 'REDE',
          usuarioId: 1,
          usuarioNome: 'João Silva',
          tecnicoId: null,
          tecnicoNome: null
        },
        {
          id: 2,
          titulo: 'Computador não liga',
          descricao: 'Meu computador não está ligando desde ontem.',
          status: 'ABERTO',
          dataCriacao: '2023-05-11T09:15:00',
          dataFinalizacao: null,
          categoria: 'HARDWARE',
          usuarioId: 1,
          usuarioNome: 'João Silva',
          tecnicoId: null,
          tecnicoNome: null
        },
        {
          id: 3,
          titulo: 'Erro no sistema',
          descricao: 'O sistema apresenta erro ao tentar salvar relatórios.',
          status: 'FINALIZADO',
          dataCriacao: '2023-05-09T11:20:00',
          dataFinalizacao: '2023-05-09T16:45:00',
          categoria: 'SOFTWARE',
          usuarioId: 1,
          usuarioNome: 'João Silva',
          tecnicoId: 2,
          tecnicoNome: 'Maria Técnica'
        }
      ];
      
      if (usuarioCodigo) {
        return chamados.filter(c => c.usuarioCodigo === usuarioCodigo);
      }
      
      return chamados;
    }
    
    // Chamada real à API
    const url = usuarioCodigo ? `/chamadas/usuario/${usuarioCodigo}` : '/chamadas';
    const response = await api.get(url);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || 'Erro ao buscar chamados');
    }
    throw error;
  }
};

export const criarChamado = async (chamadoData) => {
  try {
    // Verifica se estamos em desenvolvimento e se devemos usar dados simulados
    if (process.env.NODE_ENV === 'development' && process.env.REACT_APP_USE_MOCK_DATA === 'true') {
      return {
        id: Math.floor(Math.random() * 1000) + 4,
        ...chamadoData,
        status: 'ABERTO',
        dataCriacao: new Date().toISOString(),
        dataFinalizacao: null,
        tecnicoId: null,
        tecnicoNome: null
      };
    }
    
    // Chamada real à API
    const response = await api.post('/chamadas', chamadoData);
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || 'Erro ao criar chamado');
    }
    throw error;
  }
};

export const finalizarChamado = async (chamadaCodigo, tecnicoCodigo, tecnicoNome) => {
  try {
    // Verifica se estamos em desenvolvimento e se devemos usar dados simulados
    if (process.env.NODE_ENV === 'development' && process.env.REACT_APP_USE_MOCK_DATA === 'true') {
      return {
        id: chamadaCodigo,
        status: 'FINALIZADO',
        dataFinalizacao: new Date().toISOString(),
        tecnicoCodigo,
        tecnicoNome
      };
    }
    
    // Chamada real à API
    const response = await api.put(`/chamadas/${chamadaCodigo}/finalizar`, { tecnicoCodigo, tecnicoNome });
    return response.data;
  } catch (error) {
    if (error.response) {
      throw new Error(error.response.data.message || 'Erro ao finalizar chamado');
    }
    throw error;
  }
};

export default api;