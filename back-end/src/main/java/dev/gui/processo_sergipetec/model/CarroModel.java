package dev.gui.processo_sergipetec.model;


public class CarroModel extends VeiculoModel {
    private int quantidadePortas;
    private TiposCombustivel tipoCombustivel;

    // Construtores, Getters e Setters

    public CarroModel(int id, String modelo, String fabricante, String cor, int ano, double preco, String tipo, int quantidadePortas, TiposCombustivel tipoCombustivel) {
        super(id, modelo, fabricante, cor, ano, preco, tipo);
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }

    public CarroModel(int quantidadePortas, TiposCombustivel tipoCombustivel) {
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }

    public CarroModel() {
    }

    public int getQuantidadePortas() {
        return quantidadePortas;
    }

    public void setQuantidadePortas(int quantidadePortas) {
        this.quantidadePortas = quantidadePortas;
    }

    public TiposCombustivel getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(TiposCombustivel tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
}
