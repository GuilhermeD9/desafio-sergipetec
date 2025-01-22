package dev.gui.processo_sergipetec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MotoModel extends VeiculoModel {
    private int cilindrada;

    public MotoModel(int id, String modelo, String fabricante, int ano, double preco, int cilindrada) {
        super(id, modelo, fabricante, ano, preco);
        this.cilindrada = cilindrada;
    }
}
