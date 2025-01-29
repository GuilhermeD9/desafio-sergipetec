package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroCarro;
import dev.gui.processo_sergipetec.cadastro.CadastroMoto;
import dev.gui.processo_sergipetec.cadastro.CadastroVeiculo;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class IncluirVeiculoService {
    private final CadastroVeiculo cadastroVeiculo;
    private final CadastroCarro cadastroCarro;
    private final CadastroMoto cadastroMoto;

    public IncluirVeiculoService(CadastroVeiculo cadastroVeiculo, CadastroCarro cadastroCarro, CadastroMoto cadastroMoto) {
        this.cadastroVeiculo = cadastroVeiculo;
        this.cadastroCarro = cadastroCarro;
        this.cadastroMoto = cadastroMoto;
    }

    public void atualizarVeiculo(int id, Object veiculo) throws SQLException {
        if (veiculo instanceof CarroModel) {
            CarroModel carro = (CarroModel) veiculo;
            cadastroCarro.atualizar(id, carro);
        } else if (veiculo instanceof MotoModel) {
            MotoModel moto = (MotoModel) veiculo;
            cadastroMoto.atualizar(id, moto);
        } else {
            System.out.println("Tipo desconhecido");
        }
    }

    public void deletarVeiculo(int id) throws SQLException {
        cadastroCarro.deletar(id);
        cadastroMoto.deletar(id);
        cadastroVeiculo.deletar(id);
    }
}
