package br.com.api.suporte.servico;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.api.suporte.modelo.chamada.ChamadaModelo;
import br.com.api.suporte.modelo.chamada.ChamadaModelo.StatusChamada;
import br.com.api.suporte.repositorio.ChamadaRepositorio;

@Service
public class ChamadaServico {

    @Autowired
    private ChamadaRepositorio cr;

    // Listar todas as chamadas
    public List<ChamadaModelo> listarChamadas() {
        return StreamSupport.stream(cr.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // Listar chamadas por usuário
    public List<ChamadaModelo> listarChamadasPorUsuario(Long usuarioCodigo) {
        return cr.findByUsuarioCodigo(usuarioCodigo);
    }

    // Método para listar chamadas por status
    public Iterable<ChamadaModelo> listarPorStatus(StatusChamada status) {
        return cr.findByStatus(status);
    }

    // Método para listar chamadas por categoria
    public Iterable<ChamadaModelo> listarPorCategoria(ChamadaModelo.CategoriaChamada categoria) {
        return cr.findByCategoria(categoria);
    }

    // Buscar chamada por ID
    public ChamadaModelo buscarChamadaPorId(Long codigo) {
        Optional<ChamadaModelo> chamada = cr.findById(codigo);
        return chamada.orElse(null);
    }

    // Criar nova chamada
    public ChamadaModelo criarChamada(ChamadaModelo chamada) {
        chamada.setStatus(StatusChamada.ABERTO);
        chamada.setDataCriacao(LocalDateTime.now());
        return cr.save(chamada);
    }

    // Finalizar chamada
    public ChamadaModelo finalizarChamada(Long chamadaCodigo, Long tecnicoCodigo, String tecnicoNome) {
        Optional<ChamadaModelo> chamadaOpt = cr.findById(chamadaCodigo);

        if (chamadaOpt.isPresent()) {
            ChamadaModelo chamada = chamadaOpt.get();
            chamada.setStatus(StatusChamada.FINALIZADO);
            chamada.setDataFinalizacao(LocalDateTime.now());
            chamada.setTecnicoCodigo(tecnicoCodigo);
            chamada.setTecnicoNome(tecnicoNome);
            return cr.save(chamada);
        }

        return null;
    }

}
