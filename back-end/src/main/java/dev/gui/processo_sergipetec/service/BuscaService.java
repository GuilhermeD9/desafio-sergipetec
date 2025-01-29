package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.repository.BuscaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class BuscaService {
    private final BuscaRepository buscaRepository;

    public BuscaService(BuscaRepository buscaRepository) {
        this.buscaRepository = buscaRepository;
    }

    public VeiculoModel consultarVeiculoPorId(int id)  {
        return buscaRepository.listarVeiculoPorId(id);
    }

    public List<VeiculoModel> consultaPersonalizada(String tipo, String modelo, String cor, Integer ano) throws SQLException {
        return buscaRepository.consultarVeiculos(tipo, modelo, cor, ano);
    }
}
