package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroCarro;
import dev.gui.processo_sergipetec.model.CarroModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncluirCarroServiceTest {

    private IncluirCarroService incluirCarroService;
    private CadastroCarro cadastroCarro;

    @BeforeEach
    void setUp() {
        cadastroCarro = mock(CadastroCarro.class);
        incluirCarroService = new IncluirCarroService(cadastroCarro);
    }

    @Test
    void cadastrarCarroSuccessfully() throws SQLException {
        CarroModel carro = new CarroModel();
        incluirCarroService.cadastrarCarrro(carro);
        verify(cadastroCarro, times(1)).cadastrar(carro);
    }

    @Test
    void cadastrarCarroThrowsSQLException() throws SQLException {
        CarroModel carro = new CarroModel();
        doThrow(SQLException.class).when(cadastroCarro).cadastrar(carro);
        assertThrows(SQLException.class, () -> incluirCarroService.cadastrarCarrro(carro));
    }
}