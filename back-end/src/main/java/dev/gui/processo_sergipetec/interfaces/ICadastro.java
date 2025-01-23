package dev.gui.processo_sergipetec.interfaces;

import java.sql.SQLException;
import java.util.List;

public interface ICadastro<T> {
    void atualizar(int id, T veiculo) throws SQLException;
    void deletar(int id) throws SQLException;
    List<T> consultarTodos() throws SQLException;
    T consultarPorId(int id) throws SQLException;
}
