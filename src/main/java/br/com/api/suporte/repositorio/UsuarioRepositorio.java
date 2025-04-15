package br.com.api.suporte.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.suporte.modelo.usuario.UsuarioModelo;

@Repository
public interface UsuarioRepositorio extends CrudRepository<UsuarioModelo, Long> {
    Optional<UsuarioModelo> findByEmailIgnoreCase(String email);
    Optional<UsuarioModelo> findByEmail(String email);
}
