package dev.gui.processo_sergipetec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoModel {
    private int id;
    private String modelo;
    private String fabricante;
    private int ano;
    private double preco;
    private String tipo;

    public VeiculoModel(int id, String modelo, String fabricante, int ano, double preco) {
        this.id = id;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.ano = ano;
        this.preco = preco;
    }
}
