package dev.gui.processo_sergipetec.repository;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BuscaRepository {

    /* É capaz de fazer a busca de duas formas, passando parâmetros e limitando
        a busca, ou sem mandar nada retornando tudo do banco.
     */
    public List<VeiculoModel> consultarVeiculos(String tipo, String modelo, String cor, Integer ano) throws SQLException {
        String query = "SELECT * FROM TB_VEICULO WHERE 1=1";
        List<Object> parametros = new ArrayList<>();

        if (tipo != null) {
            switch (tipo.toLowerCase()) {
                case "carro" :
                    tipo = "Carro";
                    break;
                case "moto" :
                    tipo = "Moto";
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de veículo inválido: " + tipo);
            }
            query += " AND tipo = ?";
            parametros.add(tipo);
        }
        if (modelo != null) {
            query += " AND LOWER(modelo) = ?"; // O LOWER é para evitar problemas com caracteres maiúsculos
            parametros.add(modelo.toLowerCase());
        }

        if (cor != null) {
            query += " AND LOWER(cor) = ?";
            parametros.add(cor.toLowerCase());
        }

        if (ano != null) {
            query += " AND ano = ?";
            parametros.add(ano);
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            // Organiza os parâmetros de busca
            for (int i = 0; i < parametros.size(); i++) {
                statement.setObject(i + 1, parametros.get(i));
            }

            List<VeiculoModel> veiculos = new ArrayList<>();
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String tipoVeiculo = rs.getString("tipo");
                    VeiculoModel veiculo = mapearVeiculo(rs, tipoVeiculo, connection);
                    if (veiculo != null) veiculos.add(veiculo);
                }
            }
            return veiculos;
        }
    }

    // Consulta apenas um veículo por meio do ID
    public VeiculoModel listarVeiculoPorId(int id) {
        String query = "SELECT * FROM TB_VEICULO WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("tipo");
                    return mapearVeiculo(rs, tipo, connection);
                } else {
                    throw new RuntimeException("Veiculo com o ID " + id + " não encontrado");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar o veículo por ID no BD.");
        }
    }

    private VeiculoModel mapearVeiculo(ResultSet rs, String tipo, Connection connection) throws SQLException {
        int id = rs.getInt("id");
        String modelo = rs.getString("modelo");
        String fabricante = rs.getString("fabricante");
        String cor = rs.getString("cor");
        int ano = rs.getInt("ano");
        double preco = rs.getDouble("preco");

        return switch (tipo) {
            case "Carro" -> DetalharCarro(id, modelo, fabricante, cor, ano, preco, tipo, connection);
            case "Moto" -> DetalharMoto(id, modelo, fabricante, cor, ano, preco, tipo, connection);
            default -> null;
        };
    }

    private MotoModel DetalharMoto(int id, String modelo, String fabricante, String cor, int ano, double preco, String tipo, Connection connection) throws SQLException {
        String queryMoto = "SELECT * FROM TB_MOTO WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryMoto)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int cilindrada = rs.getInt("cilindrada");
                    return new MotoModel(id, modelo, fabricante, cor, ano, preco, tipo, cilindrada);
                }
            }
        }
        return null;
    }

    private CarroModel DetalharCarro(int id, String modelo, String fabricante, String cor, int ano, double preco, String tipo, Connection connection) throws SQLException {
        String queryCarro = "SELECT * FROM TB_CARRO WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(queryCarro)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    int quantidadePortas = rs.getInt("quantidade_portas");
                    String tipoCombustivel = rs.getString("tipo_combustivel");
                    return new CarroModel(id, modelo, fabricante, cor, ano, preco, tipo, quantidadePortas, tipoCombustivel);
                }
            }
        }
        return null;
    }
}
