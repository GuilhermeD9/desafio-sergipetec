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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    private final IncluirVeiculoService veiculoService;
    private final IncluirCarroService carroService;
    private final IncluirMotoService motoService;
    private final BuscaService buscaService;

    public VeiculoController(IncluirVeiculoService veiculoService, IncluirCarroService carroService, IncluirMotoService motoService, BuscaService buscaService) {
        this.veiculoService = veiculoService;
        this.carroService = carroService;
        this.motoService = motoService;
        this.buscaService = buscaService;
    }

    @PostMapping("/carro")
    public ResponseEntity<VeiculoModel> cadastrarCarro(@RequestBody CarroModel carro) throws SQLException {
        carroService.cadastrarCarrro(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carro);
    }

    @PostMapping("/moto")
    public ResponseEntity<VeiculoModel> cadastrarMoto(@RequestBody MotoModel moto) throws SQLException {
        motoService.cadastrarMoto(moto);
        return ResponseEntity.status(HttpStatus.CREATED).body(moto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoModel> listarVeiculoPorId(@PathVariable int id) {
        VeiculoModel veiculo = buscaService.consultarVeiculoPorId(id);
        if (veiculo != null) {
            return ResponseEntity.ok(veiculo);
        }
        else {
            return null;
        }
    }

    @GetMapping
    public ResponseEntity<PaginacaoDTO> buscaPersonalizada(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String cor,
            @RequestParam(required = false) Integer ano,
            @RequestParam(defaultValue = "id") String ordenacao,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        try {
            PaginacaoDTO veiculos = buscaService.consultarVeiculos(tipo, modelo, cor, ano, ordenacao, pagina, tamanho);
            if (veiculos.getVeiculos().isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(veiculos);
            }
            return ResponseEntity.ok(veiculos);
    } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarVeiculo(@PathVariable int id, @RequestBody VeiculoDTO veiculo) throws SQLException {
        veiculoService.atualizarVeiculo(id, veiculo);
        return ResponseEntity.ok(veiculo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarVeiculo(@PathVariable int id) throws SQLException {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.ok("Veículo deletado com sucesso! ID:" + id);
    }
}
