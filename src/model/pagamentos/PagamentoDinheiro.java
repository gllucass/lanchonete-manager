package model.pagamentos;

public class PagamentoDinheiro extends Pagamento {
    private double valorRecebido;

    public PagamentoDinheiro(double valor, double valorRecebido) {
        super(valor);
        this.valorRecebido = valorRecebido;
    }

    @Override
    public void processarPagamento() {
        if (valorRecebido < getValor()) {
            throw new RuntimeException("Valor recebido é menor que o valor do pagamento.");
        } else {
            double troco = valorRecebido - getValor();
            System.out.println("Pagamento em dinheiro processado. Troco: " + troco);
        }
    }
}