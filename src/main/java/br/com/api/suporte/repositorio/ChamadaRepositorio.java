package br.com.api.suporte.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.api.suporte.modelo.chamada.ChamadaModelo;

@Repository
public interface ChamadaRepositorio extends CrudRepository<ChamadaModelo, Long> {
    
    List<ChamadaModelo> findByUsuarioCodigo(Long usuarioCodigo);
    
    List<ChamadaModelo> findByStatus(ChamadaModelo.StatusChamada status);
    
    List<ChamadaModelo> findByCategoria(ChamadaModelo.CategoriaChamada categoria);
}
