package dev.gui.processo_sergipetec.model;


public class CarroModel extends VeiculoModel {
    private int quantidadePortas;
    private String tipoCombustivel;

    // Construtores, Getters e Setters

    public CarroModel(int id, String modelo, String fabricante, String cor, int ano, double preco, String tipo, int quantidadePortas, String tipoCombustivel) {
        super(id, modelo, fabricante, cor, ano, preco, tipo);
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
    }

    public CarroModel(int quantidadePortas, String tipoCombustivel) {
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

    public String getTipoCombustivel() {
        return tipoCombustivel;
    }

    public void setTipoCombustivel(String tipoCombustivel) {
        this.tipoCombustivel = tipoCombustivel;
    }
}
