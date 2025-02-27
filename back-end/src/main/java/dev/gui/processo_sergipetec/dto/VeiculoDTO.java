package dev.gui.processo_sergipetec.dto;

import dev.gui.processo_sergipetec.model.TiposCombustivel;

public class VeiculoDTO {
    private String modelo;
    private String fabricante;
    private String cor;
    private int ano;
    private double preco;
    private String tipo; // "Carro" ou "Moto"
    private int quantidadePortas; // Apenas para carros
    private TiposCombustivel tipoCombustivel; // Apenas para carros
    private int cilindrada; // Apenas para motos

    public VeiculoDTO(String modelo, String fabricante, String cor, int ano, double preco, String tipo, int quantidadePortas, TiposCombustivel tipoCombustivel, int cilindrada) {
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.cor = cor;
        this.ano = ano;
        this.preco = preco;
        this.tipo = tipo;
        this.quantidadePortas = quantidadePortas;
        this.tipoCombustivel = tipoCombustivel;
        this.cilindrada = cilindrada;
    }

    public VeiculoDTO() {
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public int getCilindrada() {
        return cilindrada;
    }

    public void setCilindrada(int cilindrada) {
        this.cilindrada = cilindrada;
    }
}
