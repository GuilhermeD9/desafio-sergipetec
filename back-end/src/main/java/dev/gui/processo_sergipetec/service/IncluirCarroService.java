package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroCarro;
import dev.gui.processo_sergipetec.model.CarroModel;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class IncluirCarroService {
    private final CadastroCarro cadastroCarro;

    public IncluirCarroService(CadastroCarro cadastroCarro) {
        this.cadastroCarro = cadastroCarro;
    }

    public void cadastrarCarrro(CarroModel carroModel) throws SQLException {
        cadastroCarro.cadastrarCarro(carroModel);
    }
}
