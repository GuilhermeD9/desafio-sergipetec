package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeiculoService {
    public void cadastrarVeiculo(VeiculoModel veiculo) throws SQLException {
        String query = "INSERT INTO TB_VEICULO (modelo, fabricante, ano, preco, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             statement.setString(1, veiculo.getModelo());
             statement.setString(2, veiculo.getFabricante());
             statement.setInt(3, veiculo.getAno());
             statement.setDouble(4, veiculo.getPreco());
             statement.setString(5, veiculo instanceof CarroModel ? "Carro" : "Moto");
             statement.executeUpdate();

             try (ResultSet keys = statement.getGeneratedKeys()) {
                 if (keys.next()) {
                     veiculo.setId(keys.getInt(1));
                 }
             }

             if (veiculo instanceof CarroModel carro) {
                 // TODO: CADASTRO DE CARRO
             } else if (veiculo instanceof MotoModel moto) {
                 // TODO: CADASTRO DE MOTO
             }
        }
    }

    private void cadastrarCarro(CarroModel carro) throws SQLException {
        String query = "INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, carro.getId());
            statement.setInt(2, Integer.parseInt(carro.getQuantidadePortas()));
            statement.setString(3, carro.getTipoCombustível().toString());
            statement.executeUpdate();
        }
    }

    private void cadastrarMoto(MotoModel moto) throws SQLException {
        String query = "INSERT INTO TB_MOTO (id, cilindrada) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, moto.getId());
            statement.setInt(2, moto.getCilindrada());
            statement.executeUpdate();
        }
    }

    public List<VeiculoModel> listarVeiculos() throws SQLException {
        String query = "SELECT * FROM TB_VEICULO";

        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet rs = statement.executeQuery()) {
            List<VeiculoModel> veiculos = new ArrayList<>();
            while (rs.next()) {
                VeiculoModel veiculo = new VeiculoModel(
                        rs.getInt("id"),
                        rs.getString("modelo"),
                        rs.getString("fabricante"),
                        rs.getInt("ano"),
                        rs.getDouble("preco")
                );
                veiculos.add(veiculo);
            }
            return veiculos;
        }
    }

    public VeiculoModel listarVeiculoPorId(int id) throws SQLException {
        String query = "SELECT * FROM TB_VEICULO WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new VeiculoModel(
                            rs.getInt("id"),
                            rs.getString("modelo"),
                            rs.getString("fabricante"),
                            rs.getInt("ano"),
                            rs.getDouble("preco")
                    );
                }
            }
        }
        return null;
    }

    public void atualizarVeiculo(VeiculoModel veiculo) throws SQLException {
        String query = "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, ano = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, veiculo.getModelo());
            statement.setString(2, veiculo.getFabricante());
            statement.setInt(3, veiculo.getAno());
            statement.setDouble(4, veiculo.getPreco());
            statement.setInt(5, veiculo.getId());
            statement.executeUpdate();
        }
    }

    public void excluirVeiculo(int id) throws SQLException {
        String query = "DELETE FROM TB_VEICULO WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
