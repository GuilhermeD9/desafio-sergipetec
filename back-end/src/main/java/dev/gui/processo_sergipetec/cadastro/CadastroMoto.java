package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.connection.DatabaseConnection;
import dev.gui.processo_sergipetec.interfaces.ICadastro;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Service
public class CadastroMoto implements ICadastro {
    private final CadastroVeiculo cadastroVeiculo;

    public CadastroMoto(CadastroVeiculo cadastroVeiculo) {
        this.cadastroVeiculo = cadastroVeiculo;
    }

    public void cadastrarMoto(MotoModel moto) throws SQLException {
        cadastroVeiculo.cadastrarVeiculo(moto);
        String query = "INSERT INTO TB_MOTO (id, cilindrada) VALUES (?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, moto.getId());
            statement.setInt(2, moto.getCilindrada());
            statement.executeUpdate();
        }
    }

    @Override
    public VeiculoModel atualizar(int id, VeiculoModel veiculo) throws SQLException {
        cadastroVeiculo.atualizar(id, veiculo);
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
}
