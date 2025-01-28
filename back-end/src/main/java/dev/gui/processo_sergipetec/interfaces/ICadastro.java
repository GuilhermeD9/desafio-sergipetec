package dev.gui.processo_sergipetec.interfaces;

import dev.gui.processo_sergipetec.model.VeiculoModel;

import java.sql.SQLException;

public interface ICadastro<T> {
    T atualizar(int id, VeiculoModel veiculo) throws SQLException;
    void deletar(int id) throws SQLException;
    void cadastrar(T veiculo) throws SQLException;
}
