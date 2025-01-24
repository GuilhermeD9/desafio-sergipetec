package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.interfaces.ICadastro;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.*;

@Service
public class CadastroVeiculo implements ICadastro {

    protected void cadastrarVeiculo(VeiculoModel veiculo) throws SQLException {
        String query = "INSERT INTO TB_VEICULO (modelo, fabricante, ano, preco, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             statement.setString(1, veiculo.getModelo());
             statement.setString(2, veiculo.getFabricante());
             statement.setInt(3, veiculo.getAno());
             statement.setDouble(4, veiculo.getPreco());
             statement.setString(5, veiculo.getClass().getSimpleName().replace("Model",""));
             statement.executeUpdate();

             try (ResultSet keys = statement.getGeneratedKeys()) {
                 if (keys.next()) {
                     veiculo.setId(keys.getInt(1));
                 }
             } catch (SQLException e) {
                 System.out.println("Erro ao cadastrar o veiculo: " + e.getMessage());
                 throw new RuntimeException("Erro ao cadastrar o veículo. Por favor, tente novamente.");
             }
        }
    }

    @Override
    public Object atualizar(int id, VeiculoModel veiculo) throws SQLException {
        String query = "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, ano = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, veiculo.getModelo());
            statement.setString(2, veiculo.getFabricante());
            statement.setInt(3, veiculo.getAno());
            statement.setDouble(4, veiculo.getPreco());
            statement.setInt(5, id);
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Nenhum veículo encontrado com o ID: " + id);
                throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar o veículo: " + e.getMessage());
        }
        return veiculo;
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

    protected VeiculoModel mapearVeiculo(ResultSet rs, String tipo, Connection connection) throws SQLException {
        int id = rs.getInt("id");
        String modelo = rs.getString("modelo");
        String fabricante = rs.getString("fabricante");
        int ano = rs.getInt("ano");
        double preco = rs.getDouble("preco");

        switch (tipo) {
            case "Carro":
                return DetalharCarro(id, modelo, fabricante, ano, preco, tipo, connection);
            case "Moto":
                return DetalharMoto(id, modelo, fabricante, ano, preco, tipo, connection);
            default:
                return null;
        }
    }

    private MotoModel DetalharMoto(int id, String modelo, String fabricante, int ano, double preco, String tipo, Connection connection) throws SQLException {
        String queryMoto = "SELECT * FROM TB_MOTO WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryMoto)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int cilindrada = rs.getInt("cilindrada");
                    return new MotoModel(id, modelo, fabricante, ano, preco, tipo, cilindrada);
                }
            }
        }
        return null;
    }

    private CarroModel DetalharCarro(int id, String modelo, String fabricante, int ano, double preco, String tipo, Connection connection) throws SQLException {
        String queryCarro = "SELECT * FROM TB_CARRO WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryCarro)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int quantidadePortas = rs.getInt("quantidade_portas");
                    String tipoCombustivel = rs.getString("tipo_combustivel");
                    return new CarroModel(id, modelo, fabricante, ano, preco, tipo, quantidadePortas, tipoCombustivel);
                }
            }
        }
        return null;
    }
}
