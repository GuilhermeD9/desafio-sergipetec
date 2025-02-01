package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.model.VeiculoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastroVeiculoTest {

    private VeiculoModel veiculo;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        veiculo = mock(VeiculoModel.class);
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS TB_VEICULO");
            stmt.execute("CREATE TABLE TB_VEICULO (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "modelo VARCHAR(255), " +
                    "fabricante VARCHAR(255), " +
                    "cor VARCHAR(255), " +
                    "ano INT, " +
                    "preco DOUBLE, " +
                    "tipo VARCHAR(255))");
        }
    }

    @Test
    void cadastrarVeiculoSuccessfully() throws SQLException {
        when(veiculo.getModelo()).thenReturn("Modelo");
        when(veiculo.getFabricante()).thenReturn("Fabricante");
        when(veiculo.getCor()).thenReturn("Cor");
        when(veiculo.getAno()).thenReturn(2020);
        when(veiculo.getPreco()).thenReturn(50000.0);

        try (var stmt = connection.prepareStatement(
                "INSERT INTO TB_VEICULO (modelo, fabricante, cor, ano, preco, tipo) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, veiculo.getModelo());
            stmt.setString(2, veiculo.getFabricante());
            stmt.setString(3, veiculo.getCor());
            stmt.setInt(4, veiculo.getAno());
            stmt.setDouble(5, veiculo.getPreco());
            stmt.setString(6, veiculo.getClass().getSimpleName().replace("Model", ""));
            stmt.executeUpdate();

            try (var keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    veiculo.setId(keys.getInt(1));
                }
            }
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_VEICULO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals("Modelo", rs.getString("modelo"));
            assertEquals("Fabricante", rs.getString("fabricante"));
            assertEquals("Cor", rs.getString("cor"));
            assertEquals(2020, rs.getInt("ano"));
            assertEquals(50000.0, rs.getDouble("preco"));
        }
    }

    @Test
    void atualizarVeiculoSuccessfully() throws SQLException {
        when(veiculo.getModelo()).thenReturn("Modelo");
        when(veiculo.getFabricante()).thenReturn("Fabricante");
        when(veiculo.getCor()).thenReturn("Cor");
        when(veiculo.getAno()).thenReturn(2020);
        when(veiculo.getPreco()).thenReturn(50000.0);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (modelo, fabricante, cor, ano, preco, tipo) VALUES ('OldModel', 'OldFabricante', 'OldCor', 2019, 40000.0, 'Veiculo')");
        }

        try (var stmt = connection.prepareStatement(
                "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, cor = ?, ano = ?, preco = ? WHERE id = ?")) {
            stmt.setString(1, veiculo.getModelo());
            stmt.setString(2, veiculo.getFabricante());
            stmt.setString(3, veiculo.getCor());
            stmt.setInt(4, veiculo.getAno());
            stmt.setDouble(5, veiculo.getPreco());
            stmt.setInt(6, 1);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
            }
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_VEICULO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals("Modelo", rs.getString("modelo"));
            assertEquals("Fabricante", rs.getString("fabricante"));
            assertEquals("Cor", rs.getString("cor"));
            assertEquals(2020, rs.getInt("ano"));
            assertEquals(50000.0, rs.getDouble("preco"));
        }
    }

    @Test
    void atualizarVeiculoNotFound() {
        when(veiculo.getModelo()).thenReturn("Modelo");
        when(veiculo.getFabricante()).thenReturn("Fabricante");
        when(veiculo.getCor()).thenReturn("Cor");
        when(veiculo.getAno()).thenReturn(2020);
        when(veiculo.getPreco()).thenReturn(50000.0);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            try (var stmt = connection.prepareStatement(
                    "UPDATE TB_VEICULO SET modelo = ?, fabricante = ?, cor = ?, ano = ?, preco = ? WHERE id = ?")) {
                stmt.setString(1, veiculo.getModelo());
                stmt.setString(2, veiculo.getFabricante());
                stmt.setString(3, veiculo.getCor());
                stmt.setInt(4, veiculo.getAno());
                stmt.setDouble(5, veiculo.getPreco());
                stmt.setInt(6, 1);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
                }
            }
        });

        assertEquals("Erro: Nenhum veículo encontrado com o id fornecido", exception.getMessage());
    }

    @Test
    void deletarVeiculoSuccessfully() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (modelo, fabricante, cor, ano, preco, tipo) VALUES ('Modelo', 'Fabricante', 'Cor', 2020, 50000.0, 'Veiculo')");
        }

        try (var stmt = connection.prepareStatement("DELETE FROM TB_VEICULO WHERE id = ?")) {
            stmt.setInt(1, 1);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido.");
            }
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_VEICULO WHERE id = 1")) {
            assertFalse(rs.next());
        }
    }

    @Test
    void deletarVeiculoNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            try (var stmt = connection.prepareStatement("DELETE FROM TB_VEICULO WHERE id = ?")) {
                stmt.setInt(1, 1);
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido.");
                }
            }
        });

        assertEquals("Erro: Nenhum veículo encontrado com o id fornecido.", exception.getMessage());
    }
}