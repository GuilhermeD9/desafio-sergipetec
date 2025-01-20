package dev.gui.processo_sergipetec.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VeiculoModel {
    private int id;
    private String modelo;
    private String fabricante;
    private int ano;
    private double preco;


}
