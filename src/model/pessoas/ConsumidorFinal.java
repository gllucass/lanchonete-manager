package model.pessoas;

public class ConsumidorFinal extends Pessoa {
    private int numeroMesa;

    public ConsumidorFinal(int id, String nome, String telefone, int numeroMesa) {
        super(id, nome, telefone);
        this.numeroMesa = numeroMesa;
    }

    public int getNumeroMesa() { return numeroMesa; }
}
