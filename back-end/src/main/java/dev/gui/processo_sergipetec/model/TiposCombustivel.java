package dev.gui.processo_sergipetec.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TiposCombustivel {
    GASOLINA("Gasolina"),
    ETANOL("Etanol"),
    DIESEL("Diesel"),
    FLEX("Flex");

    private final String nomeFormatado;

    TiposCombustivel(String nomeFormatado) {
        this.nomeFormatado = nomeFormatado;
    }

    @JsonValue
    public String getNomeFormatado() {
        return nomeFormatado;
    }

    @JsonCreator
    public static TiposCombustivel fromString(String nome) {
        for (TiposCombustivel tipo : TiposCombustivel.values()) {
            if (tipo.getNomeFormatado().equalsIgnoreCase(nome)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de combustível inválido: " + nome);
    }
}
