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

import br.com.api.suporte.modelo.usuario.RespostaUsuario;
import br.com.api.suporte.modelo.usuario.UsuarioModelo;
import br.com.api.suporte.servico.UsuarioServico;

@RestController
@CrossOrigin(origins = "*")
public class UsuarioControle {

    @Autowired
    private UsuarioServico us;

    // rota para listar os usuarios
    @GetMapping("/listarUsuarios")
    public Iterable<UsuarioModelo> listar() {
        return us.listar();
    }

    // rota para cadastrar usuarios
    @PostMapping("/cadastrarUsuarios")
    public ResponseEntity<?> cadastar(@RequestBody UsuarioModelo um) {
        return us.cadastrarEditar(um, "cadastar");
    }

    // rota para editar usuarios
    @PutMapping("/editarUsuarios")
    public ResponseEntity<?> editar(@RequestBody UsuarioModelo um) {
        return us.cadastrarEditar(um, "editar");
    }

    // rota para deletar usuarios
    @DeleteMapping("/deletarUsuarios/{codigo}")
    public ResponseEntity<RespostaUsuario> deletar(@PathVariable long codigo) {
        return us.deletar(codigo);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        String email = credenciais.get("email");
        String senha = credenciais.get("senha");

        Optional<UsuarioModelo> usuario = us.buscarPorEmail(email);

        Map<String, Object> resposta = new HashMap<>();

        if (usuario.isPresent() && usuario.get().getSenha().equals(senha)) {
            UsuarioModelo usuarioEncontrado = usuario.get();

            resposta.put("success", true);
            resposta.put("userType", "user");
            resposta.put("userData", Map.of(
                    "id", usuarioEncontrado.getCodigo(),
                    "name", usuarioEncontrado.getNome(),
                    "surname", usuarioEncontrado.getSobrenome(),
                    "email", usuarioEncontrado.getEmail()));

            return ResponseEntity.ok(resposta);
        }

        resposta.put("success", false);
        resposta.put("message", "Credenciais inválidas");

        return ResponseEntity.ok(resposta);
    }
}
