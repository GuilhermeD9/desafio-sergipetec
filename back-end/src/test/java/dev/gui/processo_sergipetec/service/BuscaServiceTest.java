package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.dto.PaginacaoDTO;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.repository.BuscaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BuscaServiceTest {

    private BuscaService buscaService;
    private BuscaRepository buscaRepository;

    @BeforeEach
    void setUp() {
        buscaRepository = mock(BuscaRepository.class);
        buscaService = new BuscaService(buscaRepository);
    }

    @Test
    void consultarVeiculoPorIdSuccessfully() {
        VeiculoModel veiculo = new VeiculoModel();
        when(buscaRepository.listarVeiculoPorId(1)).thenReturn(veiculo);

        VeiculoModel result = buscaService.consultarVeiculoPorId(1);

        assertEquals(veiculo, result);
        verify(buscaRepository, times(1)).listarVeiculoPorId(1);
    }

    @Test
    void consultarVeiculoPorIdNotFound() {
        when(buscaRepository.listarVeiculoPorId(999)).thenReturn(null);

        VeiculoModel result = buscaService.consultarVeiculoPorId(999);

        assertNull(result);
        verify(buscaRepository, times(1)).listarVeiculoPorId(999);
    }

    @Test
    void consultarVeiculosSuccessfully() throws SQLException {
        PaginacaoDTO paginacaoDTO = new PaginacaoDTO();
        when(buscaRepository.consultarVeiculos(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(paginacaoDTO);

        PaginacaoDTO result = buscaService.consultarVeiculos("Carro", "Modelo", "Cor", 2020, "id", 1, 10);

        assertEquals(paginacaoDTO, result);
        verify(buscaRepository, times(1)).consultarVeiculos("Carro", "Modelo", "Cor", 2020, "id", 1, 10);
    }

    @Test
    void consultarVeiculosThrowsSQLException() throws SQLException {
        when(buscaRepository.consultarVeiculos(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenThrow(SQLException.class);

        assertThrows(SQLException.class, () -> buscaService.consultarVeiculos("Carro", "Modelo", "Cor", 2020, "asc", 1, 10));
    }
}