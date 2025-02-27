package dev.gui.processo_sergipetec.connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/db_frota";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    static {
        try {
            // Registra o driver do PostgreSQL se necessário.
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Driver do PostgreSQL não encontrado. " + e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
