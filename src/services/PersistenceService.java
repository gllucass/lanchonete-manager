package services;

import model.Pedido;
import model.itens.ItemConsumivel;
import model.pessoas.ClienteCadastrado;
import model.pessoas.Funcionario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenceService {

    private static final String DIR = "dados";

    static {
        File dir = new File(DIR);
        if (!dir.exists()) dir.mkdirs();
    }

    public static void salvarFuncionarios(List<Funcionario> lista) {
        salvarLista(lista, "funcionarios.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Funcionario> carregarFuncionarios() {
        Object obj = carregarLista("funcionarios.dat");
        return obj != null ? (List<Funcionario>) obj : new ArrayList<>();
    }

    public static void salvarClientes(List<ClienteCadastrado> lista) {
        salvarLista(lista, "clientes.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<ClienteCadastrado> carregarClientes() {
        Object obj = carregarLista("clientes.dat");
        return obj != null ? (List<ClienteCadastrado>) obj : new ArrayList<>();
    }

    public static void salvarCardapio(List<ItemConsumivel> lista) {
        salvarLista(lista, "cardapio.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<ItemConsumivel> carregarCardapio() {
        Object obj = carregarLista("cardapio.dat");
        return obj != null ? (List<ItemConsumivel>) obj : new ArrayList<>();
    }

    public static void salvarPedidos(List<Pedido> lista) {
        salvarLista(lista, "pedidos.dat");
    }

    @SuppressWarnings("unchecked")
    public static List<Pedido> carregarPedidos() {
        Object obj = carregarLista("pedidos.dat");
        return obj != null ? (List<Pedido>) obj : new ArrayList<>();
    }

    public static void salvarContadores(int nextIdPessoa, int nextPedidoId) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DIR + "/contadores.dat"))) {
            oos.writeInt(nextIdPessoa);
            oos.writeInt(nextPedidoId);
        } catch (IOException e) {
            System.err.println("Erro ao salvar contadores: " + e.getMessage());
        }
    }

    public static int[] carregarContadores() {
        File arq = new File(DIR + "/contadores.dat");
        if (!arq.exists()) return new int[]{1, 1};
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arq))) {
            return new int[]{ois.readInt(), ois.readInt()};
        } catch (IOException e) {
            return new int[]{1, 1};
        }
    }

    private static void salvarLista(Object lista, String nomeArquivo) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DIR + "/" + nomeArquivo))) {
            oos.writeObject(lista);
        } catch (IOException e) {
            System.err.println("Erro ao salvar " + nomeArquivo + ": " + e.getMessage());
        }
    }

    private static Object carregarLista(String nomeArquivo) {
        File arq = new File(DIR + "/" + nomeArquivo);
        if (!arq.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(arq))) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erro ao carregar " + nomeArquivo + ": " + e.getMessage());
            return null;
        }
    }
}
