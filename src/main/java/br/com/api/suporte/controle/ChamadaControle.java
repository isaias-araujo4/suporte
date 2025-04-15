package br.com.api.suporte.controle;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.suporte.dto.FinalizarChamadaRequest;
import br.com.api.suporte.modelo.chamada.ChamadaModelo;
import br.com.api.suporte.modelo.chamada.ChamadaModelo.CategoriaChamada;
import br.com.api.suporte.modelo.chamada.ChamadaModelo.StatusChamada;
import br.com.api.suporte.servico.ChamadaServico;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/chamadas")
public class ChamadaControle {

    @Autowired
    private ChamadaServico cs;

    // Listar todas as chamadas
    @GetMapping
    public ResponseEntity<List<ChamadaModelo>> listarChamadas() {
        return ResponseEntity.ok(cs.listarChamadas());
    }

    // Criar nova chamada
    @PostMapping
    public ResponseEntity<ChamadaModelo> criarChamada(@RequestBody ChamadaModelo chamada) {
        return ResponseEntity.ok(cs.criarChamada(chamada));
    }

    // Finalizar chamada
    @PutMapping("/{chamadaCodigo}/finalizar")
    public ResponseEntity<ChamadaModelo> finalizarChamada(
            @PathVariable Long chamadaCodigo,
            @RequestBody FinalizarChamadaRequest request) {
        return ResponseEntity
                .ok(cs.finalizarChamada(chamadaCodigo, request.getTecnicoCodigo(), request.getTecnicoNome()));
    }

    // Listar chamadas por usuário
    @GetMapping("/usuario/{usuarioCodigo}")
    public ResponseEntity<List<ChamadaModelo>> listarChamadasPorUsuario(@PathVariable Long usuarioCodigo) {
        return ResponseEntity.ok(cs.listarChamadasPorUsuario(usuarioCodigo));
    }

    @GetMapping("/chamadas/status/{status}")
    public ResponseEntity<?> listarPorStatus(@PathVariable String status) {
        StatusChamada statusChamada = StatusChamada.valueOf(status.toUpperCase());
        return ResponseEntity.ok(cs.listarPorStatus(statusChamada));
    }

    @GetMapping("/chamadas/categoria/{categoria}")
    public ResponseEntity<?> listarPorCategoria(@PathVariable String categoria) {
        CategoriaChamada categoriaChamada = CategoriaChamada.valueOf(categoria.toUpperCase());
        return ResponseEntity.ok(cs.listarPorCategoria(categoriaChamada));
    }

    // Buscar chamada por ID
    @GetMapping("/{codigo}")
    public ResponseEntity<ChamadaModelo> buscarChamadaPorId(@PathVariable Long codigo) {
        ChamadaModelo chamada = cs.buscarChamadaPorId(codigo);
        if (chamada != null) {
            return ResponseEntity.ok(chamada);
        }
        return ResponseEntity.notFound().build();
    }
}
