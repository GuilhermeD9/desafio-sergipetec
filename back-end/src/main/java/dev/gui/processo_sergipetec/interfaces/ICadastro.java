package dev.gui.processo_sergipetec.interfaces;

import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;

import java.sql.SQLException;
import java.util.List;

public interface ICadastro<T> {
    T cadastrar(CarroModel carro) throws SQLException;
    T atualizar(int id, T veiculo) throws SQLException;
    void deletar(int id) throws SQLException;
    List<T> consultarTodos() throws SQLException;
    T consultarPorId(int id) throws SQLException;
}
