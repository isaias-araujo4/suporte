package br.com.api.suporte.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.suporte.modelo.tecnico.TecnicoModelo;

@Repository
public interface TecnicoRepositorio extends CrudRepository<TecnicoModelo, Long> {
    Optional<TecnicoModelo> findByEmailIgnoreCase(String email);
    Optional<TecnicoModelo> findByEmail(String email);
}
