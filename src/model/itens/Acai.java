package model.itens;

public class Acai extends ItemConsumivel {
    private String tamanho;
    private int qtdAdicionais;

    public Acai(String nome, double precoBase, String tamanho, int qtdAdicionais) {
        super(nome, precoBase);
        this.tamanho = tamanho;
        this.qtdAdicionais = qtdAdicionais;
    }

    @Override
    public double calcularPrecoFinal() {
        return getPrecoBase() + qtdAdicionais * 2.50;
    }

    public String getTamanho() { return tamanho; }
    public int getQtdAdicionais() { return qtdAdicionais; }
}
