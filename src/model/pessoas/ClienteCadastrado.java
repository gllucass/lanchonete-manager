package model.pessoas;

public class ClienteCadastrado extends Pessoa {
    private String cpf;
    private int pontosFidelidade;
    private String endereco;

    public ClienteCadastrado(int id, String nome, String telefone, String cpf, String endereco) {
        super(id, nome, telefone);
        this.cpf = cpf;
        this.endereco = endereco;
        this.pontosFidelidade = 0;
    }

    public void adicionarPontos(int pontos) {
        if (pontos > 0) {
            this.pontosFidelidade += pontos;
        }
    }

    public String getCpf() { return cpf; }
    public String getEndereco() { return endereco; }
    public int getPontosFidelidade() { return pontosFidelidade; }
}

