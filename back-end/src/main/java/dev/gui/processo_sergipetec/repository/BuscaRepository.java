package dev.gui.processo_sergipetec.repository;

import dev.gui.processo_sergipetec.cadastro.CadastroVeiculo;
import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class BuscaRepository extends CadastroVeiculo {

    public List<VeiculoModel> consultarVeiculos(String tipo, String modelo, Integer ano) throws SQLException {
        String query = "SELECT * FROM TB_VEICULO WHERE 1=1";
        List<Object> parametros = new ArrayList<>();

        if (tipo != null) {
            switch (tipo.toLowerCase()) {
                case "carro" :
                    tipo = "CarroModel";
                    break;
                case "moto" :
                    tipo = "MotoModel";
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de veículo inválido: " + tipo);
            }
            query += " AND tipo = ?";
            parametros.add(tipo);
        }
        if (modelo != null) {
            query += " AND modelo = ?";
            parametros.add(modelo);
        }

        /*if (cor != null) {
            query += " AND cor = ?";
            parametros.add(cor);
        }*/

        if (ano != null) {
            query += " AND ano = ?";
            parametros.add(ano);
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

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

    public VeiculoModel listarVeiculoPorId(int id) throws SQLException {
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
}
