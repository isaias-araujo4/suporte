package br.com.api.suporte.modelo.tecnico;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class RespostaTecnico {
    private String mensagem;
}
