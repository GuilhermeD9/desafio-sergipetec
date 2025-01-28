package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroMoto;
import dev.gui.processo_sergipetec.model.MotoModel;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class IncluirMotoService {
    private final CadastroMoto cadastroMoto;

    public IncluirMotoService(CadastroMoto cadastroMoto) {
        this.cadastroMoto = cadastroMoto;
    }

    public void cadastrarMoto(MotoModel moto) throws SQLException {
        cadastroMoto.cadastrar(moto);
    }
}
