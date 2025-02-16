package dev.gui.processo_sergipetec.connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final Dotenv dotenv = Dotenv.load(); // Pode apagar essa variável após alterar as variáveis;
    private static final String URL = dotenv.get("DATABASE_URL");
    private static final String USER = dotenv.get("DATABASE_USER"); // Usuario do SQL
    private static final String PASSWORD = dotenv.get("DATABASE_PASSWORD"); // Senha do SQL

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
