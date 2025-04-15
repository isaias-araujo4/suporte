package br.com.api.suporte.dto;

public class FinalizarChamadaRequest {
    private Long tecnicoCodigo;
    private String tecnicoNome;

    public Long getTecnicoCodigo() {
        return tecnicoCodigo;
    }

    public void setTecnicoCodigo(Long tecnicoCodigo) {
        this.tecnicoCodigo = tecnicoCodigo;
    }

    public String getTecnicoNome() {
        return tecnicoNome;
    }

    public void setTecnicoNome(String tecnicoNome) {
        this.tecnicoNome = tecnicoNome;
    }
}
