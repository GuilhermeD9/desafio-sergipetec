package dev.gui.processo_sergipetec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroModel extends VeiculoModel {
    private int quantidadePortas;
    private String tipoCombustivel;

    public CarroModel(int id, String modelo, String fabricante, int ano, double preco, String tipo, int quantidadePortas, String tipoCombustivel) {
        super(id, modelo, fabricante, ano, preco, tipo);
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }
}
