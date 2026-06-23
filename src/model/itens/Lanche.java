package model.itens;

public class Lanche extends ItemConsumivel {
    private boolean isArtesanal;

    public Lanche(String nome, double precoBase, boolean isArtesanal) {
        super(nome, precoBase);
        this.isArtesanal = isArtesanal;
    }

    @Override
    public double calcularPrecoFinal() {
        return isArtesanal ? getPrecoBase() * 1.20 : getPrecoBase();
    }

    public boolean isArtesanal() { return isArtesanal; }
}
