package dev.gui.processo_sergipetec.dto;

import dev.gui.processo_sergipetec.model.VeiculoModel;

import java.util.List;

public class PaginacaoDTO {
    private List<VeiculoModel> veiculos;
    private int totalRegistros;

    public PaginacaoDTO(List<VeiculoModel> veiculos, int totalRegistros) {
        this.veiculos = veiculos;
        this.totalRegistros = totalRegistros;
    }

    public List<VeiculoModel> getVeiculos() {
        return veiculos;
    }


    public int getTotalRegistros() {
        return totalRegistros;
    }

}
