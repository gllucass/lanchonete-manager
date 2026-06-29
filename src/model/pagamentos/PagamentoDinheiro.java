package model.pagamentos;

import exceptions.RegraNegocioException;

public class PagamentoDinheiro extends Pagamento {
    private double valorRecebido;

    public PagamentoDinheiro(double valor, double valorRecebido) {
        super(valor);
        this.valorRecebido = valorRecebido;
    }

    @Override
    public void processarPagamento() {
        if (valorRecebido < getValor()) {
            throw new RegraNegocioException("Valor recebido é menor que o valor do pagamento.");
        }
        double troco = valorRecebido - getValor();
        System.out.printf("Pagamento em dinheiro processado. Troco: R$ %.2f%n", troco);
    }

    public double getValorRecebido() { return valorRecebido; }
}