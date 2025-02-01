package dev.gui.processo_sergipetec.connection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ConnectionTest {

    @BeforeEach
    void setUp() {
        // Não é preciso configurar o mock do Dotenv.
    }

    @Test
    void getConnectionSuccessfully() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        assertNotNull(connection);
        assertFalse(connection.isClosed());
        connection.close();
    }
}