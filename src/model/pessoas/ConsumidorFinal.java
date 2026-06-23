package model.pessoas;

public class ConsumidorFinal extends Pessoa {
    // Atributos específicos e encapsulados
    private String cpf;
    private int pontosFidelidade;

    // Construtor utilizando o super() e inicializando os pontos em zero
    public ClienteCadastrado(int id, String nome, String telefone, String cpf) {
        super(id, nome, telefone);
        this.cpf = cpf;
        this.pontosFidelidade = 0; // Inicializado em zero
    }

    // Método de regra de negócio para acumular pontos
    public void adicionarPontos(int pontos) {
        if (pontos > 0) {
            this.pontosFidelidade += pontos;
        }
    }

    // Getters específicos (Conforme solicitado, sem Setters para proteger os pontos e CPF)
    public String getCpf() {
        return cpf;
    }

    public int getPontosFidelidade() {
        return pontosFidelidade;
    }
}
