package br.com.api.suporte.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.api.suporte.modelo.tecnico.RespostaTecnico;
import br.com.api.suporte.modelo.tecnico.TecnicoModelo;
import br.com.api.suporte.repositorio.TecnicoRepositorio;

@Service
public class TecnicoServico {

    @Autowired
    private TecnicoRepositorio tr;

    @Autowired
    private RespostaTecnico rt;

    // Método para buscar técnico por email
    public Optional<TecnicoModelo> buscarPorEmail(String email) {
        return tr.findByEmail(email);
    }

    // método para listar os tecnicos
    public Iterable<TecnicoModelo> listar() {
        return tr.findAll();
    }

    // método para cadastrar ou editar os tecnicos
    public ResponseEntity<?> cadastrarEditar(TecnicoModelo tm, String acao) {

        // Validação de campos obrigatórios
        if (tm.getNome() == null || tm.getNome().trim().isEmpty()) {
            rt.setMensagem("O nome do tecnico é obrigatório");
            return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.BAD_REQUEST);
        } else if (tm.getSobrenome() == null || tm.getSobrenome().trim().isEmpty()) {
            rt.setMensagem("O sobrenome do tecnico é obrigatório");
            return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.BAD_REQUEST);
        } else if (tm.getEmail() == null || tm.getEmail().trim().isEmpty()) {
            rt.setMensagem("O email do tecnico é obrigatório");
            return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.BAD_REQUEST);
        } else if (tm.getSenha() == null || tm.getSenha().trim().isEmpty()) {
            rt.setMensagem("A senha do usuário é obrigatória");
            return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.BAD_REQUEST);
        } else if (tm.getCargo() == null || tm.getCargo().trim().isEmpty()) {
            rt.setMensagem("O cargo do tecnico é obrogatorio");
            return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.BAD_REQUEST);
        }

        // Normalizando os campos (ignorando letras maiúsculas/minúsculas)
        String nome = tm.getNome().trim().toLowerCase();
        String sobrenome = tm.getSobrenome().trim().toLowerCase();
        String email = tm.getEmail().trim().toLowerCase();
        String cargo = tm.getCargo().trim().toLowerCase();

        tm.setNome(nome);
        tm.setSobrenome(sobrenome);
        tm.setEmail(email);
        tm.setCargo(cargo);

        if (acao.equals("cadastrar")) {
            Optional<TecnicoModelo> emailExistente = tr.findByEmailIgnoreCase(tm.getEmail());

            // Validação para email duplicado no cadastro
            if (emailExistente.isPresent()) {
                rt.setMensagem("Já existe um tecnico com esse email");
                return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.CONFLICT);
            }

            rt.setMensagem("O tecnico foi cadastrado com sucesso");
            return new ResponseEntity<TecnicoModelo>(tr.save(tm), HttpStatus.CREATED);

        } else {
            Optional<TecnicoModelo> emailExistente = tr.findByEmailIgnoreCase(tm.getEmail());

            // Validação para email duplicado na edição
            if (emailExistente.isPresent() && emailExistente.get().getCodigo() != tm.getCodigo()) {
                rt.setMensagem("Já existe um tecnico com esse email");
                return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.CONFLICT);
            }

            rt.setMensagem("O tecnico foi editado com sucesso");
            return new ResponseEntity<TecnicoModelo>(tr.save(tm), HttpStatus.OK);
        }
    }

    // método para deletar tecnicos
    public ResponseEntity<RespostaTecnico> deletar(Long codigo) {
        tr.deleteById(codigo);

        rt.setMensagem("O tecnico foi deletado com sucesso");
        return new ResponseEntity<RespostaTecnico>(rt, HttpStatus.OK);
    }
}
