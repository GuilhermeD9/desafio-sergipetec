package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.cadastro.CadastroCarro;
import dev.gui.processo_sergipetec.cadastro.CadastroMoto;
import dev.gui.processo_sergipetec.cadastro.CadastroVeiculo;
import dev.gui.processo_sergipetec.dto.VeiculoDTO;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

class IncluirVeiculoServiceTest {

    private IncluirVeiculoService incluirVeiculoService;
    private CadastroCarro cadastroCarro;
    private CadastroMoto cadastroMoto;
    private CadastroVeiculo cadastroVeiculo;

    @BeforeEach
    void setUp() {
        cadastroCarro = mock(CadastroCarro.class);
        cadastroMoto = mock(CadastroMoto.class);
        cadastroVeiculo = mock(CadastroVeiculo.class);
        incluirVeiculoService = new IncluirVeiculoService(cadastroVeiculo, cadastroCarro, cadastroMoto);
    }

    @Test
    void atualizarCarroSuccessfully() throws SQLException {
        VeiculoDTO veiculoDTO = new VeiculoDTO( "Modelo", "Fabricante", "Cor", 2020, 50000.0, "Carro", 4, "Gasolina", 0);
        incluirVeiculoService.atualizarVeiculo(1, veiculoDTO);
        verify(cadastroCarro, times(1)).atualizar(eq(1), any(CarroModel.class));
    }

    @Test
    void atualizarMotoSuccessfully() throws SQLException {
        VeiculoDTO veiculoDTO = new VeiculoDTO( "Modelo", "Fabricante", "Cor", 2020, 50000.0, "Moto", 0, null, 150);
        incluirVeiculoService.atualizarVeiculo(1, veiculoDTO);
        verify(cadastroMoto, times(1)).atualizar(eq(1), any(MotoModel.class));
    }

    @Test
    void atualizarVeiculoUnknownType() throws SQLException {
        VeiculoDTO veiculoDTO = new VeiculoDTO( "Modelo", "Fabricante", "Cor", 2020, 50000.0, "Caminhao", 0, null, 0);
        incluirVeiculoService.atualizarVeiculo(1, veiculoDTO);
        verify(cadastroCarro, never()).atualizar(anyInt(), any(CarroModel.class));
        verify(cadastroMoto, never()).atualizar(anyInt(), any(MotoModel.class));
    }

    @Test
    void deletarVeiculoSuccessfully() throws SQLException {
        incluirVeiculoService.deletarVeiculo(1);
        verify(cadastroCarro, times(1)).deletar(1);
        verify(cadastroMoto, times(1)).deletar(1);
        verify(cadastroVeiculo, times(1)).deletar(1);
    }
}