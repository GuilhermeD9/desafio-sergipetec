package dev.gui.processo_sergipetec.service;

import dev.gui.processo_sergipetec.dto.PaginacaoDTO;
import dev.gui.processo_sergipetec.model.VeiculoModel;
import dev.gui.processo_sergipetec.cadastro.BuscaCadastro;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class BuscaService {
    private final BuscaCadastro buscaCadastro;

    public BuscaService(BuscaCadastro buscaCadastro) {
        this.buscaCadastro = buscaCadastro;
    }

    public VeiculoModel consultarVeiculoPorId(int id)  {
        return buscaCadastro.listarVeiculoPorId(id);
    }

    public PaginacaoDTO consultarVeiculos(String tipo, String modelo, String cor, Integer ano,
                                          String ordenacao, int pagina, int tamanho) throws SQLException {
        return buscaCadastro.consultarVeiculos(tipo, modelo, cor, ano, ordenacao, pagina, tamanho);
    }
}
