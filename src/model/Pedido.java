package model;

import enums.StatusPedido;
import enums.TipoConsumo;
import model.itens.ItemConsumivel;
import model.pagamentos.Pagamento;
import model.pessoas.Pessoa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pedido implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private TipoConsumo tipoConsumo;
    private Pessoa cliente;
    private StatusPedido status;
    private List<ItemConsumivel> itens;
    private List<Pagamento> pagamentos;
    private Integer numeroMesa;

    public Pedido(int id, TipoConsumo tipoConsumo, Pessoa cliente) {
        this.id = id;
        this.tipoConsumo = tipoConsumo;
        this.cliente = cliente;
        this.status = StatusPedido.CRIADO;
        this.itens = new ArrayList<>();
        this.pagamentos = new ArrayList<>();
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemConsumivel item : itens) {
            total += item.calcularPrecoFinal();
        }
        return total;
    }

    public void adicionarItem(ItemConsumivel item) {
        this.itens.add(item);
    }

    public void adicionarPagamento(Pagamento pag) {
        this.pagamentos.add(pag);
    }

    // Getters e Setters
    public int getId() { return id; }
    public TipoConsumo getTipoConsumo() { return tipoConsumo; }
    public Pessoa getCliente() { return cliente; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public List<ItemConsumivel> getItens() { return itens; }
    public List<Pagamento> getPagamentos() { return pagamentos; }
    public Integer getNumeroMesa() { return numeroMesa; }
    public void setNumeroMesa(Integer numeroMesa) { this.numeroMesa = numeroMesa; }
}
