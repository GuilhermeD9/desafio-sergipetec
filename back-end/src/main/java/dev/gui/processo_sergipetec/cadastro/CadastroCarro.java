package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.interfaces.ICadastro;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class CadastroCarro extends CadastroVeiculo implements ICadastro {
    private final CadastroVeiculo cadastroVeiculo;

    public CadastroCarro(CadastroVeiculo cadastroVeiculo) {
        this.cadastroVeiculo = cadastroVeiculo;
    }

    public Object cadastrarCarro(CarroModel carro) throws SQLException {
        cadastroVeiculo.cadastrarVeiculo(carro); // Puxa a função base de cadastrar um veículo
        String query = "INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, carro.getId());
            statement.setInt(2, carro.getQuantidadePortas());
            statement.setString(3, carro.getTipoCombustivel());
            statement.executeUpdate();
        }
        return carro;
    }

    @Override
    public VeiculoModel atualizar(int id, VeiculoModel veiculo) throws SQLException {
        cadastroVeiculo.atualizar(id, veiculo);
        CarroModel carro = (CarroModel) veiculo;
        String queryCarro = "UPDATE TB_CARRO SET quantidade_portas = ?, tipo_combustivel = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryCarro)) {
            statement.setInt(1, carro.getQuantidadePortas());
            statement.setString(2, carro.getTipoCombustivel());
            statement.setInt(3, id);
            statement.executeUpdate();
        }
        return veiculo;
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteCarro = connection.prepareStatement("DELETE FROM TB_CARRO WHERE id = ?")) {
            deleteCarro.setInt(1, id);
            deleteCarro.executeUpdate();
        }
    }
}
