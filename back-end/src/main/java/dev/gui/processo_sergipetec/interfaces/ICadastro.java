package dev.gui.processo_sergipetec.interfaces;


import java.sql.SQLException;

public interface ICadastro<T> {
    void atualizar(int id, T veiculo) throws SQLException;
    void deletar(int id) throws SQLException;
    void cadastrar(T veiculo) throws SQLException;
}
