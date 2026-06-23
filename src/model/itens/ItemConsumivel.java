package model.itens;

public abstract class ItemConsumivel {
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
