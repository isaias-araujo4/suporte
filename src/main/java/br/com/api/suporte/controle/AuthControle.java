package br.com.api.suporte.controle;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.api.suporte.repositorio.UsuarioRepositorio;
import br.com.api.suporte.repositorio.TecnicoRepositorio;
import br.com.api.suporte.dto.LoginRequest;
import br.com.api.suporte.dto.LoginResponse;
import br.com.api.suporte.modelo.tecnico.TecnicoModelo;
import br.com.api.suporte.modelo.usuario.UsuarioModelo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthControle {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private TecnicoRepositorio tr;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Verificar se é um usuário
        Optional<UsuarioModelo> optionalUsuario = ur.findByEmail(loginRequest.getEmail());

        if (optionalUsuario.isPresent()) {
            UsuarioModelo usuario = optionalUsuario.get();

            if (usuario.getSenha().equals(loginRequest.getSenha())) {
                LoginResponse response = new LoginResponse();
                response.setId(usuario.getCodigo());
                response.setNome(usuario.getNome());
                response.setSobrenome(usuario.getSobrenome());
                response.setEmail(usuario.getEmail());
                response.setRole("USUARIO");
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos");

            }
        }

        // Verificar se é um técnico
        Optional<TecnicoModelo> optionalTecnico = tr.findByEmail(loginRequest.getEmail());

        if (optionalTecnico.isPresent()) {
            TecnicoModelo tecnico = optionalTecnico.get();

            if (tecnico.getSenha().equals(loginRequest.getSenha())) {
                LoginResponse response = new LoginResponse();
                response.setId(tecnico.getCodigo());
                response.setNome(tecnico.getNome());
                response.setSobrenome(tecnico.getSobrenome());
                response.setEmail(tecnico.getEmail());
                response.setCargo(tecnico.getCargo());
                response.setRole("TECNICO");
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body("Credenciais inválidas");
    }
}
