package dev.gui.processo_sergipetec.controller;

import dev.gui.processo_sergipetec.model.CarroModel;
import dev.gui.processo_sergipetec.model.MotoModel;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {
    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping("/cadastrar/carro")
    public ResponseEntity<String> cadastrarCarro(@RequestBody CarroModel carro) throws SQLException {
        veiculoService.cadastrarCarro(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body("Veículo cadastrado");
    }

    @PostMapping("/cadastrar/moto")
    public ResponseEntity<String> cadastrarMoto(@RequestBody MotoModel moto) throws SQLException {
        veiculoService.cadastrarMoto(moto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Veículo cadastrado");
    }

    @GetMapping("/listartodos")
    public ResponseEntity<List<VeiculoModel>> listarVeiculos() throws SQLException {
        List<VeiculoModel> veiculos = veiculoService.listarVeiculos();
        return ResponseEntity.ok(veiculos);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<String> atualizarVeiculo(@PathVariable int id, @RequestBody VeiculoModel veiculoModel) throws SQLException {
        veiculoService.atualizarVeiculo(id, veiculoModel);
        return ResponseEntity.ok("Veículo atualizado com sucesso!");
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarVeiculo(@PathVariable int id) throws SQLException {
        veiculoService.excluirVeiculo(id);
        return ResponseEntity.ok("Veículo deletado com sucesso!");
    }
}
