package br.com.api.suporte.controle;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.api.suporte.modelo.tecnico.RespostaTecnico;
import br.com.api.suporte.modelo.tecnico.TecnicoModelo;
import br.com.api.suporte.servico.TecnicoServico;

@RestController
@CrossOrigin(origins = "*")
public class TecnicoControle {

    @Autowired
    private TecnicoServico ts;

    // rota para listar os tecnicos
    @GetMapping("/listarTecnicos")
    public Iterable<TecnicoModelo> listar() {
        return ts.listar();
    }

    // rota para cadastrar tecnicos
    @PostMapping("/cadastrarTecnicos")
    public ResponseEntity<?> cadastar(@RequestBody TecnicoModelo tm) {
        return ts.cadastrarEditar(tm, "cadastar");
    }

    // rota para editar tecnicos
    @PutMapping("/editarTecnicos")
    public ResponseEntity<?> editar(@RequestBody TecnicoModelo tm) {
        return ts.cadastrarEditar(tm, "editar");
    }

    // rota para deletar tecnicos
    @DeleteMapping("/deletarTecnicos/{codigo}")
    public ResponseEntity<RespostaTecnico> deletar(@PathVariable long codigo) {
        return ts.deletar(codigo);
    }

    @PostMapping("/login-tecnico")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        Optional<TecnicoModelo> tecnico = ts.buscarPorEmail(email);

        Map<String, Object> resposta = new HashMap<>();

        if (tecnico.isPresent() && tecnico.get().getSenha().equals(senha)) {
            TecnicoModelo tecnicoEncontrado = tecnico.get();

            resposta.put("success", true);
            resposta.put("userType", "technician");
            resposta.put("userData", Map.of(
                    "id", tecnicoEncontrado.getCodigo(),
                    "name", tecnicoEncontrado.getNome(),
                    "surname", tecnicoEncontrado.getSobrenome(),
                    "email", tecnicoEncontrado.getEmail(),
                    "role", tecnicoEncontrado.getCargo()));

            return ResponseEntity.ok(resposta);
        }

        resposta.put("success", false);
        resposta.put("message", "Credenciais inválidas");

        return ResponseEntity.ok(resposta);
    }
}
