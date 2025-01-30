package dev.gui.processo_sergipetec.model;


public class MotoModel extends VeiculoModel {
    private int cilindrada;

    // Construtores, Getters e Setters

    public MotoModel(int id, String modelo, String fabricante, String cor, int ano, double preco, String tipo, int cilindrada) {
        super(id, modelo, fabricante, cor, ano, preco, tipo);
        this.cilindrada = cilindrada;
    }

    public MotoModel(int cilindrada) {
        this.cilindrada = cilindrada;
    }

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
}
