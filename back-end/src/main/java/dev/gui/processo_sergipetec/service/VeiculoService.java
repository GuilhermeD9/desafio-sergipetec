package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class VeiculoService {
    private void cadastrarVeiculo(VeiculoModel veiculo) throws SQLException {
        String query = "INSERT INTO TB_VEICULO (modelo, fabricante, ano, preco, tipo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
             statement.setString(1, veiculo.getModelo());
             statement.setString(2, veiculo.getFabricante());
             statement.setInt(3, veiculo.getAno());
             statement.setDouble(4, veiculo.getPreco());
             statement.setString(5, veiculo.getClass().getSimpleName());
             statement.executeUpdate();

             try (ResultSet keys = statement.getGeneratedKeys()) {
                 if (keys.next()) {
                     veiculo.setId(keys.getInt(1));
                 }
             }
        }
    }

    public void cadastrarCarro(CarroModel carro) throws SQLException {
        cadastrarVeiculo(carro);
        String query = "INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, carro.getId());
            statement.setInt(2, carro.getQuantidadePortas());
            statement.setString(3, carro.getTipoCombustivel());
            statement.executeUpdate();
        }
    }

    public void cadastrarMoto(MotoModel moto) throws SQLException {
        cadastrarVeiculo(moto);
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
                    String tipo = rs.getString("tipo");
                    VeiculoModel veiculo = mapearVeiculo(rs, tipo, connection);
                    if (veiculo != null) veiculos.add(veiculo);
            }
            return veiculos;
        }
    }

    public void atualizarVeiculo(int id, VeiculoModel veiculo) throws SQLException {
        String query = "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, ano = ?, preco = ? WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Atualiza os dados comuns do veículo na tabela TB_VEICULO
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, veiculo.getModelo());
                statement.setString(2, veiculo.getFabricante());
                statement.setInt(3, veiculo.getAno());
                statement.setDouble(4, veiculo.getPreco());
                statement.setInt(5, id);
                statement.executeUpdate();
            }

            // Verifica o tipo do veículo e atualiza as tabelas específicas
            if (veiculo instanceof CarroModel) {
                CarroModel carro = (CarroModel) veiculo;
                String queryCarro = "UPDATE TB_CARRO SET quantidade_portas = ?, tipo_combustivel = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(queryCarro)) {
                    statement.setInt(1, carro.getQuantidadePortas());
                    statement.setString(2, carro.getTipoCombustivel());
                    statement.setInt(3, id);
                    statement.executeUpdate();
                }
            } else if (veiculo instanceof MotoModel) {
                MotoModel moto = (MotoModel) veiculo;
                String queryMoto = "UPDATE TB_MOTO SET cilindrada = ? WHERE id = ?";
                try (PreparedStatement statement = connection.prepareStatement(queryMoto)) {
                    statement.setInt(1, moto.getCilindrada());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                }
            }
        }
    }

    public void excluirVeiculo(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            try ( PreparedStatement deleteCarro = connection.prepareStatement("DELETE FROM TB_CARRO WHERE id = ?")) {
                deleteCarro.setInt(1, id);
                deleteCarro.executeUpdate();
            }

            try ( PreparedStatement deleteMoto = connection.prepareStatement("DELETE FROM TB_MOTO WHERE id = ?")) {
                deleteMoto.setInt(1, id);
                deleteMoto.executeUpdate();
            }

            try ( PreparedStatement deleteVeiculo = connection.prepareStatement("DELETE FROM TB_VEICULO WHERE id = ?")) {
                deleteVeiculo.setInt(1, id);
                deleteVeiculo.executeUpdate();
            }
        }
    }

    public VeiculoModel listarVeiculoPorId(int id) throws SQLException {
        String query = "SELECT * FROM TB_VEICULO WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    return mapearVeiculo(rs, tipo, connection);
                }
            }
        }
        return null;
    }

    private VeiculoModel mapearVeiculo(ResultSet rs, String tipo, Connection connection) throws SQLException {
        int id = rs.getInt("id");
        String modelo = rs.getString("modelo");
        String fabricante = rs.getString("fabricante");
        int ano = rs.getInt("ano");
        double preco = rs.getDouble("preco");
        String tipagem = tipo;

        switch (tipo) {
            case "CarroModel":
                return DetalharCarro(id, modelo, fabricante, ano, preco, tipo, connection);
            case "MotoModel":
                return DetalharMoto(id, modelo, fabricante, ano, preco, tipo, connection);
            default:
                return null;
        }
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
}
