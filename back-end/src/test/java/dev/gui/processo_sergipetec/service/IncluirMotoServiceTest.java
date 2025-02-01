package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroMoto;
import dev.gui.processo_sergipetec.model.MotoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncluirMotoServiceTest {

    private IncluirMotoService incluirMotoService;
    private CadastroMoto cadastroMoto;

    @BeforeEach
    void setUp() {
        cadastroMoto = mock(CadastroMoto.class);
        incluirMotoService = new IncluirMotoService(cadastroMoto);
    }

    @Test
    void cadastrarMotoSuccessfully() throws SQLException {
        MotoModel moto = new MotoModel();
        incluirMotoService.cadastrarMoto(moto);
        verify(cadastroMoto, times(1)).cadastrar(moto);
    }

    @Test
    void cadastrarMotoThrowsSQLException() throws SQLException {
        MotoModel moto = new MotoModel();
        doThrow(SQLException.class).when(cadastroMoto).cadastrar(moto);
        assertThrows(SQLException.class, () -> incluirMotoService.cadastrarMoto(moto));
    }
}