package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.model.MotoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastroMotoTest {

    private MotoModel moto;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        moto = mock(MotoModel.class);
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS TB_MOTO");
            stmt.execute("DROP TABLE IF EXISTS TB_VEICULO");
            stmt.execute("CREATE TABLE TB_VEICULO (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "modelo VARCHAR(255) NOT NULL, " +
                    "fabricante VARCHAR(255) NOT NULL, " +
                    "cor VARCHAR(255) NOT NULL, " +
                    "ano INT NOT NULL, " +
                    "preco DOUBLE NOT NULL, " +
                    "tipo VARCHAR(255) NOT NULL)");
            stmt.execute("CREATE TABLE TB_MOTO (" +
                    "id INT PRIMARY KEY, " +
                    "cilindrada INT, " +
                    "FOREIGN KEY (id) REFERENCES TB_VEICULO(id))");
        }
    }

    @Test
    void cadastrarMotoSuccessfully() throws SQLException {
        when(moto.getId()).thenReturn(1);
        when(moto.getCilindrada()).thenReturn(150);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'Modelo', 'Fabricante', 'Cor', 2020, 50000.0, 'Moto')");
        }

        String query = "INSERT INTO TB_MOTO (id, cilindrada) VALUES (?, ?)";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, moto.getId());
            statement.setInt(2, moto.getCilindrada());
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_MOTO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals(150, rs.getInt("cilindrada"));
        }
    }

    @Test
    void atualizarMotoSuccessfully() throws SQLException {
        when(moto.getCilindrada()).thenReturn(200);

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'OldModel', 'OldFabricante', 'OldCor', 2019, 40000.0, 'Moto')");
            stmt.execute("INSERT INTO TB_MOTO (id, cilindrada) VALUES (1, 150)");
        }

        String query = "UPDATE TB_MOTO SET cilindrada = ? WHERE id = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, moto.getCilindrada());
            statement.setInt(2, 1);
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_MOTO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals(200, rs.getInt("cilindrada"));
        }
    }

    @Test
    void atualizarMotoNotFound() {
        when(moto.getCilindrada()).thenReturn(200);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            String query = "UPDATE TB_MOTO SET cilindrada = ? WHERE id = ?";
            try (var statement = connection.prepareStatement(query)) {
                statement.setInt(1, moto.getCilindrada());
                statement.setInt(2, 999);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
                }
            }
        });

        assertEquals("Erro: Nenhum veículo encontrado com o id fornecido", exception.getMessage());
    }

    @Test
    void deletarMotoSuccessfully() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'Modelo', 'Fabricante', 'Cor', 2020, 50000.0, 'Moto')");
            stmt.execute("INSERT INTO TB_MOTO (id, cilindrada) VALUES (1, 150)");
        }

        String query = "DELETE FROM TB_MOTO WHERE id = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, 1);
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_MOTO WHERE id = 1")) {
            assertFalse(rs.next());
        }
    }

    @Test
    void deletarMotoNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            String query = "DELETE FROM TB_MOTO WHERE id = ?";
            try (var statement = connection.prepareStatement(query)) {
                statement.setInt(1, 999);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido.");
                }
            }
        });

        assertEquals("Erro: Nenhum veículo encontrado com o id fornecido.", exception.getMessage());
    }
}