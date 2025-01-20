package dev.gui.processo_sergipetec.connection;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Conexão estabelecida.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar com o PostgreSQL: ");
            e.printStackTrace();
        }
    }
}
