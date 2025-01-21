package dev.gui.processo_sergipetec.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MotoModel extends VeiculoModel {
    private int cilindrada;
}
