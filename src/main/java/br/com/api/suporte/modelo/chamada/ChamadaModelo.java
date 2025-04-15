package br.com.api.suporte.modelo.chamada;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "chamadas")
@Getter
@Setter
public class ChamadaModelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;
    private String titulo;
    private String descricao;
    @Enumerated(EnumType.STRING)
    private StatusChamada status;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFinalizacao;
    @Enumerated(EnumType.STRING)
    private CategoriaChamada categoria;
    
    private Long usuarioCodigo;
    
    private String usuarioNome;
    
    private Long tecnicoCodigo;
    
    private String tecnicoNome;
    
    public enum StatusChamada {
        ABERTO, FINALIZADO
    }
    
    public enum CategoriaChamada {
        REDE, HARDWARE, SOFTWARE
    }
}
