package dev.gui.processo_sergipetec.controller;

import dev.gui.processo_sergipetec.dto.PaginacaoDTO;
import dev.gui.processo_sergipetec.dto.VeiculoDTO;
import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.service.BuscaService;
import dev.gui.processo_sergipetec.service.IncluirCarroService;
import dev.gui.processo_sergipetec.service.IncluirMotoService;
import dev.gui.processo_sergipetec.service.IncluirVeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VeiculoControllerTest {

    private VeiculoController veiculoController;
    private IncluirVeiculoService veiculoService;
    private IncluirCarroService carroService;
    private IncluirMotoService motoService;
    private BuscaService buscaService;

    @BeforeEach
    void setUp() {
        veiculoService = mock(IncluirVeiculoService.class);
        carroService = mock(IncluirCarroService.class);
        motoService = mock(IncluirMotoService.class);
        buscaService = mock(BuscaService.class);
        veiculoController = new VeiculoController(veiculoService, carroService, motoService, buscaService);
    }

    @Test
    void cadastrarCarroSuccessfully() throws SQLException {
        CarroModel carro = new CarroModel();
        ResponseEntity<VeiculoModel> response = veiculoController.cadastrarCarro(carro);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(carro, response.getBody());
        verify(carroService, times(1)).cadastrarCarrro(carro);
    }

    @Test
    void cadastrarMotoSuccessfully() throws SQLException {
        MotoModel moto = new MotoModel();
        ResponseEntity<VeiculoModel> response = veiculoController.cadastrarMoto(moto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(moto, response.getBody());
        verify(motoService, times(1)).cadastrarMoto(moto);
    }

    @Test
    void listarVeiculoPorIdSuccessfully() {
        VeiculoModel veiculo = new VeiculoModel();
        when(buscaService.consultarVeiculoPorId(1)).thenReturn(veiculo);
        ResponseEntity<VeiculoModel> response = veiculoController.listarVeiculoPorId(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(veiculo, response.getBody());
        verify(buscaService, times(1)).consultarVeiculoPorId(1);
    }

    @Test
    void listarVeiculoPorIdNotFound() {
        when(buscaService.consultarVeiculoPorId(999)).thenReturn(null);
        ResponseEntity<VeiculoModel> response = veiculoController.listarVeiculoPorId(999);
        assertNull(response);
        verify(buscaService, times(1)).consultarVeiculoPorId(999);
    }

    @Test
    void buscaPersonalizadaSuccessfully() throws SQLException {
        PaginacaoDTO paginacaoDTO = new PaginacaoDTO();
        paginacaoDTO.setVeiculos(new ArrayList<>());
        paginacaoDTO.getVeiculos().add(new VeiculoModel());
        when(buscaService.consultarVeiculos(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(paginacaoDTO);
        ResponseEntity<PaginacaoDTO> response = veiculoController.buscaPersonalizada("Carro", "Modelo", "Cor", 2020, "id", 0, 10);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paginacaoDTO, response.getBody());
        verify(buscaService, times(1)).consultarVeiculos("Carro", "Modelo", "Cor", 2020, "id", 0, 10);
    }

    @Test
    void buscaPersonalizadaNotFound() throws SQLException {
        PaginacaoDTO paginacaoDTO = new PaginacaoDTO();
        paginacaoDTO.setVeiculos(new ArrayList<>()); // Ensure the list is not null
        when(buscaService.consultarVeiculos(anyString(), anyString(), anyString(), anyInt(), anyString(), anyInt(), anyInt()))
                .thenReturn(paginacaoDTO);
        ResponseEntity<PaginacaoDTO> response = veiculoController.buscaPersonalizada("Carro", "Modelo", "Cor", 2020, "id", 0, 10);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(paginacaoDTO, response.getBody());
        verify(buscaService, times(1)).consultarVeiculos("Carro", "Modelo", "Cor", 2020, "id", 0, 10);
    }

    @Test
    void atualizarVeiculoSuccessfully() throws SQLException {
        VeiculoDTO veiculoDTO = new VeiculoDTO("Modelo", "Fabricante", "Cor", 2020, 50000.0, "Carro", 4, "Gasolina", 0);
        ResponseEntity<Object> response = veiculoController.atualizarVeiculo(1, veiculoDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(veiculoDTO, response.getBody());
        verify(veiculoService, times(1)).atualizarVeiculo(1, veiculoDTO);
    }

    @Test
    void deletarVeiculoSuccessfully() throws SQLException {
        ResponseEntity<String> response = veiculoController.deletarVeiculo(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Veículo deletado com sucesso! ID:1", response.getBody());
        verify(veiculoService, times(1)).deletarVeiculo(1);
    }
}