package services;

import enums.StatusPedido;
import enums.TipoConsumo;
import exceptions.EstadoPedidoInvalidoException;
import exceptions.RegraNegocioException;
import model.pessoas.ClienteCadastrado;
import model.Pedido;
import model.pagamentos.Pagamento;

public class PedidoService {

    // Regra: Delivery/Retirada exige um ClienteCadastrado
    public void validarCriacaoPedido(Pedido pedido) {
        if ((pedido.getTipoConsumo() == TipoConsumo.DELIVERY || pedido.getTipoConsumo() == TipoConsumo.RETIRADA) &&
                !(pedido.getCliente() instanceof ClienteCadastrado)) {
            throw new RegraNegocioException("Pedidos para Delivery ou Retirada exigem um Cliente Cadastrado no sistema.");
        }
    }

    // Regra: Verifica a quitação múltipla do pedido
    public void verificarQuitacao(Pedido pedido) {
        double totalPago = 0;
        for (Pagamento p : pedido.getPagamentos()) {
            totalPago += p.getValor();
        }

        double totalDevido = pedido.calcularTotal();

        if (totalPago >= totalDevido && pedido.getStatus() == StatusPedido.CRIADO) {
            pedido.setStatus(StatusPedido.PAGO);
        } else if (totalPago < totalDevido) {
            throw new RegraNegocioException(String.format("Valor pago é insuficiente. Resta: R$ %.2f", (totalDevido - totalPago)));
        }
    }

    // Regra: Máquina de estado dinâmica e sequencial
    public void avancarStatus(Pedido pedido) {
        switch (pedido.getStatus()) {
            case CRIADO:
                throw new EstadoPedidoInvalidoException("O pedido ainda não foi pago. Impossível avançar para preparo.");
            case PAGO:
                pedido.setStatus(StatusPedido.PREPARANDO);
                break;
            case PREPARANDO:
                pedido.setStatus(StatusPedido.PRONTO);
                break;
            case PRONTO:
                pedido.setStatus(StatusPedido.ENTREGUE);
                break;
            case ENTREGUE:
                throw new EstadoPedidoInvalidoException("Este pedido já foi finalizado e entregue ao cliente.");
        }
    }
}