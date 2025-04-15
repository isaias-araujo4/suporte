package br.com.api.suporte.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.api.suporte.modelo.usuario.RespostaUsuario;
import br.com.api.suporte.modelo.usuario.UsuarioModelo;
import br.com.api.suporte.repositorio.UsuarioRepositorio;

@Service
public class UsuarioServico {

    @Autowired
    private UsuarioRepositorio ur;

    @Autowired
    private RespostaUsuario ru;

    // método para listar os usuarios
    public Iterable<UsuarioModelo> listar() {
        return ur.findAll();
    }

    // Método para buscar usuário por email
    public Optional<UsuarioModelo> buscarPorEmail(String email) {
        return ur.findByEmail(email);
    }

    // método para cadastrar ou editar os usuarios
    public ResponseEntity<?> cadastrarEditar(UsuarioModelo um, String acao) {

        // Validação de campos obrigatórios
        if (um.getNome() == null || um.getNome().trim().isEmpty()) {
            ru.setMensagem("O nome do usuário é obrigatório");
            return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.BAD_REQUEST);
        } else if (um.getSobrenome() == null || um.getSobrenome().trim().isEmpty()) {
            ru.setMensagem("O sobrenome do usuário é obrigatório");
            return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.BAD_REQUEST);
        } else if (um.getEmail() == null || um.getEmail().trim().isEmpty()) {
            ru.setMensagem("O email do usuário é obrigatório");
            return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.BAD_REQUEST);
        } else if (um.getSenha() == null || um.getSenha().trim().isEmpty()) {
            ru.setMensagem("A senha do usuário é obrigatória");
            return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.BAD_REQUEST);
        }

        // Normalizando os campos (ignorando letras maiúsculas/minúsculas)
        String nome = um.getNome().trim().toLowerCase();
        String sobrenome = um.getSobrenome().trim().toLowerCase();
        String email = um.getEmail().trim().toLowerCase();

        um.setNome(nome);
        um.setSobrenome(sobrenome);
        um.setEmail(email);

        if (acao.equals("cadastrar")) {
            Optional<UsuarioModelo> emailExistente = ur.findByEmailIgnoreCase(um.getEmail());

            // Validação para email duplicado no cadastro
            if (emailExistente.isPresent()) {
                ru.setMensagem("Já existe um usuário com esse email");
                return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.CONFLICT);
            }

            ru.setMensagem("O usuario foi cadastrado com sucesso");
            return new ResponseEntity<UsuarioModelo>(ur.save(um), HttpStatus.CREATED);

        } else {
            Optional<UsuarioModelo> emailExistente = ur.findByEmailIgnoreCase(um.getEmail());

            // Validação para email duplicado na edição
            if (emailExistente.isPresent() && emailExistente.get().getCodigo() != um.getCodigo()) {
                ru.setMensagem("Já existe um usuário com esse email");
                return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.CONFLICT);
            }

            ru.setMensagem("O usuario foi editado com sucesso");
            return new ResponseEntity<UsuarioModelo>(ur.save(um), HttpStatus.OK);
        }
    }

    // método para deletar usuarios
    public ResponseEntity<RespostaUsuario> deletar(Long codigo) {
        ur.deleteById(codigo);

        ru.setMensagem("O usuario foi deletado com sucesso");
        return new ResponseEntity<RespostaUsuario>(ru, HttpStatus.OK);
    }
}
