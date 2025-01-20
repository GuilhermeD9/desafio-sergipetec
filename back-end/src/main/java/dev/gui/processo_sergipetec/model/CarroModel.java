package dev.gui.processo_sergipetec.model;

import dev.gui.processo_sergipetec.TipoCombustível;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarroModel extends VeiculoModel {
    private String quantidadePortas;
    private TipoCombustível tipoCombustível;
}
