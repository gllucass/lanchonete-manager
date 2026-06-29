package model.pagamentos;

import java.io.Serializable;

public abstract class Pagamento implements Serializable {
    private static final long serialVersionUID = 1L;
    private double valor;

    public Pagamento(double valor) {
        this.valor = valor;
    }

    public abstract void processarPagamento();

    public double getValor() { return valor; }
}
