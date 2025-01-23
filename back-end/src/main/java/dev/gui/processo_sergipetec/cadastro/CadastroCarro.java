package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.interfaces.ICadastro;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.service.VeiculoService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CadastroCarro implements ICadastro {
    private final VeiculoService veiculoService;

    public CadastroCarro(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @Override
    public Object cadastrar(CarroModel carro) throws SQLException {
        veiculoService.cadastrarVeiculo(carro);
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
    public Object atualizar(int id, Object veiculo) throws SQLException {
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

    @Override
    public List<VeiculoModel> consultarTodos() throws SQLException {
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

    @Override
    public Object consultarPorId(int id) throws SQLException {
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

        switch (tipo) {
            case "CarroModel":
                return DetalharCarro(id, modelo, fabricante, ano, preco, tipo, connection);
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
}
