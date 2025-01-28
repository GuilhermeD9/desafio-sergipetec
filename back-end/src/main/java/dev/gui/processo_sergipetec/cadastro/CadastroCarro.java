package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Service
public class CadastroCarro extends CadastroVeiculo<CarroModel> {
    // Restrição dos tipos de combustível
    private static final Set<String> TIPOS_COMBUSTIVEL = new HashSet<>(Set.of("Gasolina", "Etanol", "Diesel", "Flex"));

    @Override
    public void cadastrar(CarroModel carro) throws SQLException {
        validarTipoCombustivel(carro.getTipoCombustivel());
        super.cadastrar(carro); // Puxa a função base de cadastrar um veículo
        String query = "INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, carro.getId());
            statement.setInt(2, carro.getQuantidadePortas());
            statement.setString(3, carro.getTipoCombustivel());
            statement.executeUpdate();
        }
    }

    @Override
    public CarroModel atualizar(int id, VeiculoModel veiculo) throws SQLException {
        super.atualizar(id, veiculo);
        CarroModel carro = (CarroModel) veiculo;
        validarTipoCombustivel(carro.getTipoCombustivel());

        String queryCarro = "UPDATE TB_CARRO SET quantidade_portas = ?, tipo_combustivel = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryCarro)) {
            statement.setInt(1, carro.getQuantidadePortas());
            statement.setString(2, carro.getTipoCombustivel());
            statement.setInt(3, id);
            statement.executeUpdate();
        }
        return carro;
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteCarro = connection.prepareStatement("DELETE FROM TB_CARRO WHERE id = ?")) {
            deleteCarro.setInt(1, id);
            deleteCarro.executeUpdate();
        }
    }

    private void validarTipoCombustivel(String tipoCombustivel) {
        if (!TIPOS_COMBUSTIVEL.contains(tipoCombustivel)) {
            throw new IllegalArgumentException("Tipo de combustível inválido: " + tipoCombustivel);
        }
    }
}
