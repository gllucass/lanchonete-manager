package model.pagamentos;

public class PagamentoCartao extends Pagamento {
    private String tipo;

    public PagamentoCartao(double valor, String tipo) {
        super(valor);
        this.tipo = tipo;
    }

    @Override
    public void processarPagamento() {
        System.out.println("Pagamento processado: Tipo = " + this.tipo + ", Valor = " + getValor());
    }
}
