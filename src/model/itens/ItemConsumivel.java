package model.itens;

import java.io.Serializable;

public abstract class ItemConsumivel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private double precoBase;

    public ItemConsumivel(String nome, double precoBase) {
        this.nome = nome;
        this.precoBase = precoBase;
    }

    public abstract double calcularPrecoFinal();

    public String getNome() { return nome; }
    public double getPrecoBase() { return precoBase; }
}
