package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroCarro;
import dev.gui.processo_sergipetec.cadastro.CadastroMoto;
import dev.gui.processo_sergipetec.cadastro.CadastroVeiculo;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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

    public List<VeiculoModel> buscarTodosVeiculos() throws SQLException {
        return cadastroVeiculo.listarVeiculos();
    }

    public VeiculoModel buscarVeiculoPorId(int id) throws SQLException {
        return cadastroVeiculo.listarVeiculoPorId(id);
    }

    public Object atualizarVeiculo(int id, VeiculoModel veiculo) throws SQLException {
        return cadastroVeiculo.atualizar(id, veiculo);
    }

    public void deletarVeiculo(int id) throws SQLException {
        cadastroCarro.deletar(id);
        cadastroMoto.deletar(id);
        cadastroVeiculo.deletar(id);
    }
}
