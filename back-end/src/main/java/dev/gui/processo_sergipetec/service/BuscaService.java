package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.dto.PaginacaoDTO;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.repository.BuscaRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class BuscaService {
    private final BuscaRepository buscaRepository;

    public BuscaService(BuscaRepository buscaRepository) {
        this.buscaRepository = buscaRepository;
    }

    public VeiculoModel consultarVeiculoPorId(int id)  {
        return buscaRepository.listarVeiculoPorId(id);
    }

    public PaginacaoDTO consultarVeiculos(String tipo, String modelo, String cor, Integer ano,
                                          String ordenacao, int pagina, int tamanho) throws SQLException {
        return buscaRepository.consultarVeiculos(tipo, modelo, cor, ano, ordenacao, pagina, tamanho);
    }
}
