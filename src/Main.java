import enums.StatusPedido;
import enums.TipoConsumo;
import exceptions.EstadoPedidoInvalidoException;
import exceptions.RegraNegocioException;
import model.Pedido;
import model.itens.Acai;
import model.itens.ItemConsumivel;
import model.itens.Lanche;
import model.pagamentos.Pagamento;
import model.pagamentos.PagamentoCartao;
import model.pagamentos.PagamentoDinheiro;
import model.pessoas.ClienteCadastrado;
import model.pessoas.ConsumidorFinal;
import model.pessoas.Funcionario;
import services.PedidoService;
import services.PersistenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Funcionario> funcionarios = new ArrayList<>();
    private static final List<ClienteCadastrado> clientesCadastrados = new ArrayList<>();
    private static final ConsumidorFinal consumidorFinal = new ConsumidorFinal();
    private static final List<ItemConsumivel> cardapio = new ArrayList<>();
    private static final List<Pedido> pedidos = new ArrayList<>();
    private static final PedidoService pedidoService = new PedidoService();
    private static int nextIdPessoa = 1;
    private static int nextPedidoId = 1;
    private static Funcionario funcionarioLogado;

    public static void main(String[] args) {
        carregarDados();

        if (funcionarios.isEmpty()) {
            funcionarios.add(new Funcionario(1, "Administrador", "admin", "admin123"));
        }

        Runtime.getRuntime().addShutdownHook(new Thread(Main::salvarDados));

        if (cardapio.isEmpty()) {
            cardapio.add(new Lanche("X-Burger", 15.00, false));
            cardapio.add(new Lanche("X-Salada", 18.00, false));
            cardapio.add(new Lanche("X-Tudo", 25.00, true));
            cardapio.add(new Lanche("X-Bacon", 22.00, false));
            cardapio.add(new Acai("Acai Pequeno", 12.00, "300ml", 0));
            cardapio.add(new Acai("Acai Medio", 15.00, "400ml", 2));
            cardapio.add(new Acai("Acai Grande", 18.00, "500ml", 3));
        }

        if (!login()) return;

        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        LANCHONETE MANAGER");
            System.out.println("========================================");
            System.out.println("Funcionario: " + funcionarioLogado.getNome());
            System.out.println("----------------------------------------");
            System.out.println("1. Gerenciar Clientes");
            System.out.println("2. Gerenciar Cardapio");
            System.out.println("3. Gerenciar Pedidos");
            System.out.println("4. Pagamentos");
            System.out.println("5. Gerenciar Funcionarios");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> menuClientes();
                case "2" -> menuCardapio();
                case "3" -> menuPedidos();
                case "4" -> menuPagamentos();
                case "5" -> menuFuncionarios();
                case "0" -> {
                    System.out.println("Encerrando o sistema...");
                    return;
                }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static boolean login() {
        for (int tentativas = 3; tentativas > 0; tentativas--) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        LANCHONETE MANAGER");
            System.out.println("========================================");
            System.out.println("           ACESSO AO SISTEMA");
            System.out.println("----------------------------------------");
            System.out.print("Usuario: ");
            String user = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            for (Funcionario f : funcionarios) {
                if (f.getUsername().equals(user) && f.getSenha().equals(senha)) {
                    funcionarioLogado = f;
                    return true;
                }
            }
            if (tentativas > 1) {
                System.out.printf("Usuario ou senha incorretos. Restam %d tentativa(s).%n", tentativas - 1);
                pausar();
            } else {
                System.out.println("Acesso bloqueado.");
                pausar();
            }
        }
        return false;
    }

    static void limparTela() {
        for (int i = 0; i < 40; i++) System.out.println();
    }

    static void pausar() {
        System.out.print("\nPressione ENTER para continuar...");
        scanner.nextLine();
    }

    static Double lerDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido! Digite um numero.");
            }
        }
    }

    static Integer lerInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido! Digite um numero inteiro.");
            }
        }
    }

    // ========== FUNCIONARIOS ==========

    static void menuFuncionarios() {
        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("     GERENCIAR FUNCIONARIOS");
            System.out.println("========================================");
            System.out.println("1. Cadastrar Funcionario");
            System.out.println("2. Listar Funcionarios");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarFuncionario();
                case "2" -> listarFuncionarios();
                case "0" -> { return; }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static void cadastrarFuncionario() {
        limparTela();
        System.out.println("--- Cadastro de Funcionario ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Usuario: ");
        String username = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        for (Funcionario f : funcionarios) {
            if (f.getUsername().equals(username)) {
                System.out.println("Usuario ja existe!");
                pausar();
                return;
            }
        }

        funcionarios.add(new Funcionario(nextIdPessoa++, nome, username, senha));
        PersistenceService.salvarFuncionarios(funcionarios);
        PersistenceService.salvarContadores(nextIdPessoa, nextPedidoId);
        System.out.println("Funcionario cadastrado com sucesso!");
        pausar();
    }

    static void listarFuncionarios() {
        limparTela();
        System.out.println("--- FUNCIONARIOS ---");
        for (Funcionario f : funcionarios) {
            System.out.printf("ID: %d | Nome: %s | Usuario: %s%n",
                    f.getId(), f.getNome(), f.getUsername());
        }
        pausar();
    }

    // ========== CLIENTES ==========

    static void menuClientes() {
        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        GERENCIAR CLIENTES");
            System.out.println("========================================");
            System.out.println("1. Cadastrar Cliente (CPF)");
            System.out.println("2. Listar Clientes");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> cadastrarClienteCadastrado();
                case "2" -> listarClientes();
                case "0" -> { return; }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static void cadastrarClienteCadastrado() {
        limparTela();
        System.out.println("--- Cadastro de Cliente (CPF) ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("CPF: ");
        String cpf = scanner.nextLine();
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();

        ClienteCadastrado cliente = new ClienteCadastrado(nextIdPessoa++, nome, telefone, cpf, endereco);
        clientesCadastrados.add(cliente);
        PersistenceService.salvarClientes(clientesCadastrados);
        PersistenceService.salvarContadores(nextIdPessoa, nextPedidoId);
        System.out.println("Cliente cadastrado com sucesso! ID: " + cliente.getId());
        pausar();
    }

    static void listarClientes() {
        limparTela();
        System.out.println("--- Consumidor Final (padrao) ---");
        System.out.printf("ID: %d | %s%n", consumidorFinal.getId(), consumidorFinal.getNome());
        System.out.println("\n--- Clientes Cadastrados (CPF) ---");
        if (clientesCadastrados.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
        } else {
            for (ClienteCadastrado c : clientesCadastrados) {
                System.out.printf("ID: %d | Nome: %s | Tel: %s | CPF: %s | Endereco: %s | Pontos: %d%n",
                        c.getId(), c.getNome(), c.getTelefone(), c.getCpf(), c.getEndereco(), c.getPontosFidelidade());
            }
        }
        pausar();
    }

    // ========== CARDAPIO ==========

    static void menuCardapio() {
        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        GERENCIAR CARDAPIO");
            System.out.println("========================================");
            System.out.println("1. Adicionar Lanche");
            System.out.println("2. Adicionar Acai");
            System.out.println("3. Listar Cardapio");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> adicionarLanche();
                case "2" -> adicionarAcai();
                case "3" -> listarCardapio();
                case "0" -> { return; }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static void adicionarLanche() {
        limparTela();
        System.out.println("--- Adicionar Lanche ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        double preco = lerDouble("Preco Base: ");
        System.out.print("Artesanal? (s/n): ");
        boolean artesanal = scanner.nextLine().equalsIgnoreCase("s");

        cardapio.add(new Lanche(nome, preco, artesanal));
        PersistenceService.salvarCardapio(cardapio);
        System.out.println("Lanche adicionado ao cardapio!");
        pausar();
    }

    static void adicionarAcai() {
        limparTela();
        System.out.println("--- Adicionar Acai ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        double preco = lerDouble("Preco Base: ");
        System.out.print("Tamanho (ex.: 300ml, 500ml): ");
        String tamanho = scanner.nextLine();
        int adicionais = lerInt("Quantidade de adicionais: ");

        cardapio.add(new Acai(nome, preco, tamanho, adicionais));
        PersistenceService.salvarCardapio(cardapio);
        System.out.println("Acai adicionado ao cardapio!");
        pausar();
    }

    static void listarCardapio() {
        limparTela();
        System.out.println("--- CARDAPIO ---");
        if (cardapio.isEmpty()) {
            System.out.println("Cardapio vazio.");
        } else {
            for (int i = 0; i < cardapio.size(); i++) {
                ItemConsumivel item = cardapio.get(i);
                String tipo = item instanceof Lanche ? "Lanche" : "Acai";
                System.out.printf("%d. [%s] %s - R$ %.2f (Preco Final: R$ %.2f)%n",
                        i + 1, tipo, item.getNome(), item.getPrecoBase(), item.calcularPrecoFinal());
            }
        }
        pausar();
    }

    // ========== PEDIDOS ==========

    static void menuPedidos() {
        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        GERENCIAR PEDIDOS");
            System.out.println("========================================");
            System.out.println("1. Novo Pedido");
            System.out.println("2. Avancar Status do Pedido");
            System.out.println("3. Listar Pedidos");
            System.out.println("4. Ver Detalhes do Pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> novoPedido();
                case "2" -> avancarStatusPedido();
                case "3" -> listarPedidos();
                case "4" -> verDetalhesPedido();
                case "0" -> { return; }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static void novoPedido() {
        limparTela();
        System.out.println("--- Novo Pedido ---");

        System.out.println("Tipo de Consumo:");
        System.out.println("1. LOCAL (Consumo no local)");
        System.out.println("2. DELIVERY (Entrega)");
        System.out.println("3. RETIRADA (Retirar no balcao)");
        System.out.print("Escolha: ");
        String tipoOpt = scanner.nextLine();

        TipoConsumo tipo;
        switch (tipoOpt) {
            case "2" -> tipo = TipoConsumo.DELIVERY;
            case "3" -> tipo = TipoConsumo.RETIRADA;
            default -> tipo = TipoConsumo.LOCAL;
        }

        if (tipo == TipoConsumo.LOCAL) {
            System.out.println("\nSelecione o Cliente:");
            System.out.printf("1. %s (padrao)%n", consumidorFinal.getNome());
            int idx = 2;
            for (ClienteCadastrado c : clientesCadastrados) {
                System.out.printf("%d. %s (CPF: %s)%n", idx++, c.getNome(), c.getCpf());
            }

            int clienteOpt = lerInt("Escolha o cliente (numero): ");
            if (clienteOpt < 1 || clienteOpt > 1 + clientesCadastrados.size()) {
                System.out.println("Cliente invalido!");
                pausar();
                return;
            }

            var cliente = (clienteOpt == 1) ? consumidorFinal
                    : clientesCadastrados.get(clienteOpt - 2);

            Pedido pedido = new Pedido(nextPedidoId++, tipo, cliente);

            try {
                pedidoService.validarCriacaoPedido(pedido);
            } catch (RegraNegocioException e) {
                System.out.println("Erro: " + e.getMessage());
                pausar();
                return;
            }

            System.out.print("Numero da mesa (opcional, deixe em branco para pular): ");
            String mesaStr = scanner.nextLine();
            if (!mesaStr.isBlank()) {
                try {
                    pedido.setNumeroMesa(Integer.parseInt(mesaStr));
                } catch (NumberFormatException e) {
                    System.out.println("Mesa invalida, ignorando.");
                }
            }

            if (!adicionarItensAoPedido(pedido)) return;

            pedidos.add(pedido);
            PersistenceService.salvarPedidos(pedidos);
            PersistenceService.salvarContadores(nextIdPessoa, nextPedidoId);
            System.out.printf("Pedido #%d criado! Total: R$ %.2f%n",
                    pedido.getId(), pedido.calcularTotal());
            processarPagamentoPedido(pedido);
            return;
        }

        int idx = 1;
        for (ClienteCadastrado c : clientesCadastrados) {
            System.out.printf("%d. %s (CPF: %s)%n", idx++, c.getNome(), c.getCpf());
        }

        if (clientesCadastrados.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado. Cadastre um cliente primeiro!");
            pausar();
            return;
        }

        int clienteOpt = lerInt("Escolha o cliente (numero): ");
        if (clienteOpt < 1 || clienteOpt > clientesCadastrados.size()) {
            System.out.println("Cliente invalido!");
            pausar();
            return;
        }
        var cliente = clientesCadastrados.get(clienteOpt - 1);

        Pedido pedido = new Pedido(nextPedidoId++, tipo, cliente);

        try {
            pedidoService.validarCriacaoPedido(pedido);
        } catch (RegraNegocioException e) {
            System.out.println("Erro: " + e.getMessage());
            pausar();
            return;
        }

        if (!adicionarItensAoPedido(pedido)) return;

        pedidos.add(pedido);
        PersistenceService.salvarPedidos(pedidos);
        PersistenceService.salvarContadores(nextIdPessoa, nextPedidoId);
        System.out.printf("Pedido #%d criado! Total: R$ %.2f%n",
                pedido.getId(), pedido.calcularTotal());
        processarPagamentoPedido(pedido);
    }

    static boolean adicionarItensAoPedido(Pedido pedido) {
        while (true) {
            limparTela();
            System.out.println("--- Pedido #" + pedido.getId() + " ---");
            System.out.printf("Cliente: %s | ", pedido.getCliente().getNome());
            System.out.printf("Tipo: %s", pedido.getTipoConsumo());
            if (pedido.getNumeroMesa() != null) {
                System.out.printf(" | Mesa: %d", pedido.getNumeroMesa());
            }
            System.out.println("\n----------------------------------------");

            if (pedido.getItens().isEmpty()) {
                System.out.println("Nenhum item adicionado ainda.");
            } else {
                System.out.println("Itens no pedido:");
                for (int i = 0; i < pedido.getItens().size(); i++) {
                    ItemConsumivel item = pedido.getItens().get(i);
                    System.out.printf("  %d. %s - R$ %.2f%n", i + 1, item.getNome(), item.calcularPrecoFinal());
                }
                System.out.printf("  ----------------------\n  TOTAL: R$ %.2f%n", pedido.calcularTotal());
            }

            System.out.println("\n--- Cardapio Disponivel ---");
            for (int i = 0; i < cardapio.size(); i++) {
                ItemConsumivel item = cardapio.get(i);
                String tipoItem = item instanceof Lanche ? "Lanche" : "Acai";
                System.out.printf("%d. [%s] %s - R$ %.2f%n", i + 1, tipoItem, item.getNome(), item.calcularPrecoFinal());
            }
            System.out.println("\n99. Remover item do pedido");
            System.out.println("0. Finalizar pedido");

            int escolha = lerInt("Escolha um item para adicionar: ");

            if (escolha == 0) break;

            if (escolha == 99) {
                if (pedido.getItens().isEmpty()) {
                    System.out.println("Nao ha itens para remover.");
                } else {
                    int remover = lerInt("Digite o numero do item que deseja remover: ");
                    if (remover >= 1 && remover <= pedido.getItens().size()) {
                        ItemConsumivel removido = pedido.getItens().remove(remover - 1);
                        System.out.printf("Item '%s' removido do pedido.%n", removido.getNome());
                    } else {
                        System.out.println("Numero invalido!");
                    }
                }
                pausar();
                continue;
            }

            if (escolha < 1 || escolha > cardapio.size()) {
                System.out.println("Item invalido!");
                pausar();
                continue;
            }

            pedido.adicionarItem(cardapio.get(escolha - 1));
            System.out.printf("Item '%s' adicionado! Total atual: R$ %.2f%n",
                    cardapio.get(escolha - 1).getNome(), pedido.calcularTotal());
            pausar();
        }

        if (pedido.getItens().isEmpty()) {
            System.out.println("Pedido sem itens. Cancelando...");
            pausar();
            return false;
        }
        return true;
    }

    static void avancarStatusPedido() {
        limparTela();
        Pedido pedido = selecionarPedido();
        if (pedido == null) return;

        try {
            pedidoService.avancarStatus(pedido);
            PersistenceService.salvarPedidos(pedidos);
            System.out.printf("Pedido #%d agora esta: %s%n", pedido.getId(), pedido.getStatus());
        } catch (EstadoPedidoInvalidoException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        pausar();
    }

    static void listarPedidos() {
        limparTela();
        System.out.println("--- LISTA DE PEDIDOS ---");
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
        } else {
            for (Pedido p : pedidos) {
                String mesa = p.getNumeroMesa() != null ? " | Mesa: " + p.getNumeroMesa() : "";
                System.out.printf("Pedido #%d | Cliente: %s | Tipo: %s | Status: %s%s | Itens: %d | Total: R$ %.2f%n",
                        p.getId(), p.getCliente().getNome(), p.getTipoConsumo(),
                        p.getStatus(), mesa, p.getItens().size(), p.calcularTotal());
            }
        }
        pausar();
    }

    static void verDetalhesPedido() {
        limparTela();
        Pedido pedido = selecionarPedido();
        if (pedido == null) return;

        System.out.println("--- Detalhes do Pedido #" + pedido.getId() + " ---");
        System.out.println("Cliente: " + pedido.getCliente().getNome());
        System.out.println("Tipo de Consumo: " + pedido.getTipoConsumo());
        if (pedido.getNumeroMesa() != null) {
            System.out.println("Mesa: " + pedido.getNumeroMesa());
        }
        System.out.println("Status: " + pedido.getStatus());

        System.out.println("\nItens:");
        if (pedido.getItens().isEmpty()) {
            System.out.println("  Nenhum item.");
        } else {
            for (ItemConsumivel item : pedido.getItens()) {
                String desc = item instanceof Lanche l
                        ? (l.isArtesanal() ? " (Artesanal)" : "")
                        : item instanceof Acai a
                        ? " (" + a.getTamanho() + ", " + a.getQtdAdicionais() + " adicionais)"
                        : "";
                System.out.printf("  - %s%s: R$ %.2f%n", item.getNome(), desc, item.calcularPrecoFinal());
            }
        }
        System.out.printf("\nTotal: R$ %.2f%n", pedido.calcularTotal());

        System.out.println("\nPagamentos:");
        if (pedido.getPagamentos().isEmpty()) {
            System.out.println("  Nenhum pagamento registrado.");
        } else {
            double totalPago = 0;
            for (Pagamento pag : pedido.getPagamentos()) {
                totalPago += pag.getValor();
                if (pag instanceof PagamentoDinheiro d) {
                    double troco = d.getValorRecebido() - d.getValor();
                    System.out.printf("  - Dinheiro: R$ %.2f (Recebido: R$ %.2f, Troco: R$ %.2f)%n",
                            d.getValor(), d.getValorRecebido(), troco);
                } else if (pag instanceof PagamentoCartao c) {
                    String label = c.getTipo().equals("credito") ? "Credito" : "Debito";
                    System.out.printf("  - Cartao (%s): R$ %.2f%n", label, c.getValor());
                }
            }
            System.out.printf("  Total pago: R$ %.2f%n", totalPago);
        }
        pausar();
    }

    // ========== PAGAMENTOS ==========

    static void menuPagamentos() {
        while (true) {
            limparTela();
            System.out.println("========================================");
            System.out.println("        PAGAMENTOS");
            System.out.println("========================================");
            System.out.println("1. Adicionar Pagamento ao Pedido");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> adicionarPagamento();
                case "0" -> { return; }
                default -> {
                    System.out.println("Opcao invalida!");
                    pausar();
                }
            }
        }
    }

    static void processarPagamentoPedido(Pedido pedido) {
        if (pedido.getStatus() == StatusPedido.PAGO) return;

        double totalDevido = pedido.calcularTotal();
        double totalPago = pedido.getPagamentos().stream().mapToDouble(Pagamento::getValor).sum();
        double restante = Math.round((totalDevido - totalPago) * 100) / 100.0;

        if (restante <= 0) return;

        System.out.print("Deseja registrar pagamento agora? (s/n): ");
        String resposta = scanner.nextLine();
        if (!resposta.equalsIgnoreCase("s")) return;

        pagarPedido(pedido, restante);
    }

    static void pagarPedido(Pedido pedido, double restante) {
        System.out.printf("\nTotal: R$ %.2f | Restante: R$ %.2f%n", pedido.calcularTotal(), restante);
        System.out.println("Forma de Pagamento:");
        System.out.println("1. Dinheiro");
        System.out.println("2. Cartao de Credito");
        System.out.println("3. Cartao de Debito");
        System.out.print("Escolha: ");
        String forma = scanner.nextLine();

        switch (forma) {
            case "1" -> pagarDinheiro(pedido, restante);
            case "2" -> pagarCartao(pedido, restante, "credito");
            case "3" -> pagarCartao(pedido, restante, "debito");
            default -> System.out.println("Opcao invalida!");
        }
    }

    static void pagarDinheiro(Pedido pedido, double restante) {
        double valorRecebido = lerDouble("Quanto o cliente pagou? R$ ");

        double valorPagamento = Math.min(valorRecebido, restante);

        try {
            var pagamento = new PagamentoDinheiro(valorPagamento, valorRecebido);
            pagamento.processarPagamento();
            pedido.adicionarPagamento(pagamento);
            finalizarPagamento(pedido);
        } catch (RegraNegocioException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    static void pagarCartao(Pedido pedido, double restante, String tipo) {
        String label = tipo.equals("credito") ? "credito" : "debito";
        double valor = lerDouble("Valor a pagar no " + label + ": R$ ");

        if (valor > restante) valor = restante;

        var pagamento = new PagamentoCartao(valor, tipo);
        pagamento.processarPagamento();
        pedido.adicionarPagamento(pagamento);
        finalizarPagamento(pedido);
    }

    static void finalizarPagamento(Pedido pedido) {
        try {
            pedidoService.verificarQuitacao(pedido);
            PersistenceService.salvarPedidos(pedidos);
            System.out.printf("Pedido #%d quitado! Status: %s%n",
                    pedido.getId(), pedido.getStatus());
        } catch (RegraNegocioException e) {
            PersistenceService.salvarPedidos(pedidos);
            double totalDevido = pedido.calcularTotal();
            double totalPago = pedido.getPagamentos().stream().mapToDouble(Pagamento::getValor).sum();
            System.out.printf("Pagamento registrado. Restante: R$ %.2f%n",
                    Math.round((totalDevido - totalPago) * 100) / 100.0);
        }
        pausar();
    }

    static void adicionarPagamento() {
        limparTela();
        Pedido pedido = selecionarPedido();
        if (pedido == null) return;

        if (pedido.getStatus() == StatusPedido.PAGO) {
            System.out.println("Este pedido ja esta pago!");
            pausar();
            return;
        }

        double totalDevido = pedido.calcularTotal();
        double totalPago = pedido.getPagamentos().stream().mapToDouble(Pagamento::getValor).sum();
        double restante = Math.round((totalDevido - totalPago) * 100) / 100.0;

        pagarPedido(pedido, restante);
    }

    // ========== UTILITARIOS ==========

    static Pedido selecionarPedido() {
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido registrado.");
            pausar();
            return null;
        }
        for (Pedido p : pedidos) {
            System.out.printf("ID: %d | Cliente: %s | Status: %s%n",
                    p.getId(), p.getCliente().getNome(), p.getStatus());
        }
        int id = lerInt("Digite o ID do pedido: ");

        for (Pedido p : pedidos) {
            if (p.getId() == id) return p;
        }
        System.out.println("Pedido nao encontrado!");
        return null;
    }

    // ========== PERSISTENCIA ==========

    static void carregarDados() {
        List<Funcionario> funcs = PersistenceService.carregarFuncionarios();
        if (!funcs.isEmpty()) {
            funcionarios.clear();
            funcionarios.addAll(funcs);
        }

        List<ClienteCadastrado> clis = PersistenceService.carregarClientes();
        if (!clis.isEmpty()) {
            clientesCadastrados.clear();
            clientesCadastrados.addAll(clis);
        }

        List<ItemConsumivel> itens = PersistenceService.carregarCardapio();
        if (!itens.isEmpty()) {
            cardapio.clear();
            cardapio.addAll(itens);
        }

        List<Pedido> peds = PersistenceService.carregarPedidos();
        if (!peds.isEmpty()) {
            pedidos.clear();
            pedidos.addAll(peds);
        }

        int[] contadores = PersistenceService.carregarContadores();
        nextIdPessoa = contadores[0];
        nextPedidoId = contadores[1];
    }

    static void salvarDados() {
        PersistenceService.salvarFuncionarios(funcionarios);
        PersistenceService.salvarClientes(clientesCadastrados);
        PersistenceService.salvarCardapio(cardapio);
        PersistenceService.salvarPedidos(pedidos);
        PersistenceService.salvarContadores(nextIdPessoa, nextPedidoId);
    }
}
