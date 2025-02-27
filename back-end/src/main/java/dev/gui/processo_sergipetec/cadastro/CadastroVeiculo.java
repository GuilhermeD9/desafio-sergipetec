package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class CadastroVeiculo<T extends VeiculoModel> implements ICadastro<T> {

    @Override
    public void cadastrar(T veiculo) throws SQLException {
        String query = "INSERT INTO TB_VEICULO (modelo, fabricante, cor, ano, preco, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, veiculo.getModelo());
            statement.setString(2, veiculo.getFabricante());
            statement.setString(3, veiculo.getCor());
            statement.setInt(4, veiculo.getAno());
            statement.setDouble(5, veiculo.getPreco());
            statement.setString(6, veiculo.getClass().getSimpleName().replace("Model", ""));
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    veiculo.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public void atualizar(int id, T veiculo) throws SQLException {
        String query = "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, cor = ?, ano = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, veiculo.getModelo());
            statement.setString(2, veiculo.getFabricante());
            statement.setString(3, veiculo.getCor());
            statement.setInt(4, veiculo.getAno());
            statement.setDouble(5, veiculo.getPreco());
            statement.setInt(6, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nenhum veículo encontrado com o ID: " + id);
                throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o veículo: " + e.getMessage());
        }
    }

    @Override
    public void deletar(int id) throws SQLException {
        String query = "DELETE FROM TB_VEICULO WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nenhum veículo encontrado com o ID: " + id);
                throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido.");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao deletar o veículo: " + e.getMessage());
        }
    }
}
