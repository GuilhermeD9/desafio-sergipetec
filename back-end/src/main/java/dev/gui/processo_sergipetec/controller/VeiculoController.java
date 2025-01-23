package dev.gui.processo_sergipetec.controller;

import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.service.IncluirCarroService;
import dev.gui.processo_sergipetec.service.IncluirMotoService;
import dev.gui.processo_sergipetec.cadastro.CadastroVeiculo;
import dev.gui.processo_sergipetec.service.IncluirVeiculoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    private final IncluirVeiculoService veiculoService;
    private final IncluirCarroService carroService;
    private final IncluirMotoService motoService;

    public VeiculoController(IncluirVeiculoService veiculoService, IncluirCarroService carroService, IncluirMotoService motoService) {
        this.veiculoService = veiculoService;
        this.carroService = carroService;
        this.motoService = motoService;
    }

    @PostMapping("/cadastrar/carro")
    public ResponseEntity<VeiculoModel> cadastrarCarro(@RequestBody CarroModel carro) throws SQLException {
        carroService.cadastrarCarrro(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(carro);
    }

    @PostMapping("/cadastrar/moto")
    public ResponseEntity<VeiculoModel> cadastrarMoto(@RequestBody MotoModel moto) throws SQLException {
        motoService.cadastrarMoto(moto);
        return ResponseEntity.status(HttpStatus.CREATED).body(moto);
    }

    @GetMapping("/listartodos")
    public ResponseEntity<List<VeiculoModel>> listarVeiculos() throws SQLException {
        List<VeiculoModel> veiculos = veiculoService.buscarTodosVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/listar/{id}")
    public ResponseEntity<VeiculoModel> listarVeiculoPorId(@PathVariable int id) throws SQLException {
        VeiculoModel veiculo = veiculoService.buscarVeiculoPorId(id);
        if (veiculo != null) {
            return ResponseEntity.ok(veiculo);
        }
        else {
            return null;
        }
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<VeiculoModel> atualizarVeiculo(@PathVariable int id, @RequestBody VeiculoModel veiculo) throws SQLException {
        veiculoService.atualizarVeiculo(id, veiculo);
        return ResponseEntity.ok(veiculo);
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarVeiculo(@PathVariable int id) throws SQLException {
        veiculoService.deletarVeiculo(id);
        return ResponseEntity.ok("Veículo deletado com sucesso! ID:" + id);
    }
}
