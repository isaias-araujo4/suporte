package br.com.api.suporte.modelo.chamada;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class RespostaChamada {
    private String mensagem;
}
