package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.interfaces.ICadastro;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.service.VeiculoService;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Service
public class CadastroMoto implements ICadastro {
    private final VeiculoService veiculoService;

    public CadastroMoto(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    public void cadastrarMoto(MotoModel moto) throws SQLException {
        veiculoService.cadastrarVeiculo(moto);
        String query = "INSERT INTO TB_MOTO (id, cilindrada) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, moto.getId());
            statement.setInt(2, moto.getCilindrada());
            statement.executeUpdate();
        }
    }

    @Override
    public Object atualizar(int id, VeiculoModel veiculo) throws SQLException {
        veiculoService.atualizarVeiculo(id, veiculo);
        MotoModel moto = (MotoModel) veiculo;
        String queryMoto = "UPDATE TB_MOTO SET cilindrada = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(queryMoto)) {
            statement.setInt(1, moto.getCilindrada());
            statement.setInt(2, id);
            statement.executeUpdate();
        }
        return moto;
    }

    @Override
    public void deletar(int id) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement deleteMoto = connection.prepareStatement("DELETE FROM TB_MOTO WHERE id = ?")) {
            deleteMoto.setInt(1, id);
            deleteMoto.executeUpdate();
        }
    }

    @Override
    public List consultarTodos() throws SQLException {
        return List.of();
    }

    @Override
    public Object consultarPorId(int id) throws SQLException {
        return null;
    }
}
