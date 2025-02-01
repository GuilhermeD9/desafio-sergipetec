package dev.gui.processo_sergipetec.cadastro;

import dev.gui.processo_sergipetec.model.CarroModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CadastroCarroTest {

    private CarroModel carro;
    private Connection connection;

    @BeforeEach
    void setUp() throws SQLException {
        carro = mock(CarroModel.class);
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS TB_CARRO");
            stmt.execute("DROP TABLE IF EXISTS TB_VEICULO");
            stmt.execute("CREATE TABLE TB_VEICULO (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "modelo VARCHAR(255) NOT NULL, " +
                    "fabricante VARCHAR(255) NOT NULL, " +
                    "cor VARCHAR(255) NOT NULL, " +
                    "ano INT NOT NULL, " +
                    "preco DOUBLE NOT NULL, " +
                    "tipo VARCHAR(255) NOT NULL)");
            stmt.execute("CREATE TABLE TB_CARRO (" +
                    "id INT PRIMARY KEY, " +
                    "quantidade_portas INT, " +
                    "tipo_combustivel VARCHAR(255), " +
                    "FOREIGN KEY (id) REFERENCES TB_VEICULO(id))");
        }
    }

    @Test
    void cadastrarCarroSuccessfully() throws SQLException {
        when(carro.getId()).thenReturn(1);
        when(carro.getQuantidadePortas()).thenReturn(4);
        when(carro.getTipoCombustivel()).thenReturn("Gasolina");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'Modelo', 'Fabricante', 'Cor', 2020, 50000.0, 'Carro')");
        }

        String query = "INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (?, ?, ?)";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, carro.getId());
            statement.setInt(2, carro.getQuantidadePortas());
            statement.setString(3, carro.getTipoCombustivel());
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_CARRO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals(4, rs.getInt("quantidade_portas"));
            assertEquals("Gasolina", rs.getString("tipo_combustivel"));
        }
    }

    @Test
    void atualizarCarroSuccessfully() throws SQLException {
        when(carro.getQuantidadePortas()).thenReturn(2);
        when(carro.getTipoCombustivel()).thenReturn("Etanol");

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'OldModel', 'OldFabricante', 'OldCor', 2019, 40000.0, 'Carro')");
            stmt.execute("INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (1, 4, 'Gasolina')");
        }

        String query = "UPDATE TB_CARRO SET quantidade_portas = ?, tipo_combustivel = ? WHERE id = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, carro.getQuantidadePortas());
            statement.setString(2, carro.getTipoCombustivel());
            statement.setInt(3, 1);
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_CARRO WHERE id = 1")) {
            assertTrue(rs.next());
            assertEquals(2, rs.getInt("quantidade_portas"));
            assertEquals("Etanol", rs.getString("tipo_combustivel"));
        }
    }

    @Test
    void atualizarCarroNotFound() {
        when(carro.getQuantidadePortas()).thenReturn(2);
        when(carro.getTipoCombustivel()).thenReturn("Etanol");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            String query = "UPDATE TB_CARRO SET quantidade_portas = ?, tipo_combustivel = ? WHERE id = ?";
            try (var statement = connection.prepareStatement(query)) {
                statement.setInt(1, carro.getQuantidadePortas());
                statement.setString(2, carro.getTipoCombustivel());
                statement.setInt(3, 999);
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected == 0) {
                    throw new RuntimeException("Erro: Nenhum veículo encontrado com o id fornecido");
                }
            }
        });

        assertEquals("Erro: Nenhum veículo encontrado com o id fornecido", exception.getMessage());
    }

    @Test
    void deletarCarroSuccessfully() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO TB_VEICULO (id, modelo, fabricante, cor, ano, preco, tipo) VALUES (1, 'Modelo', 'Fabricante', 'Cor', 2020, 50000.0, 'Carro')");
            stmt.execute("INSERT INTO TB_CARRO (id, quantidade_portas, tipo_combustivel) VALUES (1, 4, 'Gasolina')");
        }

        String query = "DELETE FROM TB_CARRO WHERE id = ?";
        try (var statement = connection.prepareStatement(query)) {
            statement.setInt(1, 1);
            statement.executeUpdate();
        }

        try (Statement stmt = connection.createStatement();
             var rs = stmt.executeQuery("SELECT * FROM TB_CARRO WHERE id = 1")) {
            assertFalse(rs.next());
        }
    }

    @Test
    void deletarCarroNotFound() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            String query = "DELETE FROM TB_CARRO WHERE id = ?";
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