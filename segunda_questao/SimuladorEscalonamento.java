package segunda_questao;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class SimuladorEscalonamento {

    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Processo> filaProntos = new ArrayList<>();
    private static final List<Processo> filaFinalizados = new ArrayList<>();
    private static int quantum;
    private static int opcao;

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao Mini Simulador de Escalonamento de Processos!");
        simulador();
    }

    private static void simulador() {
        escolherAlgoritmo();
        System.out.println("Defina o tempo de quantum (em unidades de tempo):");
        quantum = scanner.nextInt();

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("0. Alterar algoritmo de escalonamento");
            System.out.println("1. Criar novo processo");
            System.out.println("2. Mostrar fila de prontos");
            System.out.println("3. Iniciar execução");
            System.out.println("4. Sair");
            int escolha = scanner.nextInt();

            switch (escolha) {
                case 0:
                    escolherAlgoritmo();
                    break;
                case 1:
                    criarProcesso();
                    break;
                case 2:
                    mostrarFilaProntos();
                    break;
                case 3:
                    if (filaProntos.isEmpty()) {
                        System.out.println("Nenhum processo na fila de prontos para executar.");
                    } else {
                        if (opcao == 1) {
                            executarRoundRobin();
                        } else {
                            executarPrioridadePreemptiva();
                        }
                    }
                    break;
                case 4:
                    System.out.println("Simulador encerrado.");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void escolherAlgoritmo() {
        System.out.println("\nEscolha o algoritmo de escalonamento:");
        System.out.println("1. Round Robin (RR)");
        System.out.println("2. Prioridade Preemptiva");
        opcao = scanner.nextInt();
    }

    private static void criarProcesso() {
        System.out.println("Criação de novo processo:");
        System.out.print("ID: ");
        int id = scanner.nextInt();
        System.out.print("Nome: ");
        String nome = scanner.next();
        System.out.print("Prioridade (1 a 10, menor é mais prioritário): ");
        int prioridade = scanner.nextInt();
        System.out.print("Tipo (1 para I/O-bound, 2 para CPU-bound): ");
        boolean tipoIO = scanner.nextInt() == 1;
        System.out.print("Tempo total de CPU (em unidades de tempo): ");
        int tempoCPU = scanner.nextInt();
        filaProntos.add(new Processo(id, nome, prioridade, tipoIO, tempoCPU));
        System.out.println("Processo adicionado à fila de prontos.");
    }

    private static void mostrarFilaProntos() {
        System.out.println("\nFila de Prontos:");
    
        if (filaProntos.isEmpty()) {
            System.out.println("Nenhum processo na fila.");
        } else {
            // Ordena a fila por prioridade se o algoritmo for Prioridade Preemptiva
            if (opcao == 2) { // Verifica se o algoritmo atual é Prioridade Preemptiva
                filaProntos.sort(Comparator.comparingInt(p -> p.prioridade));
            }
    
            // Exibe os processos na fila
            for (int i = 0; i < filaProntos.size(); i++) {
                Processo processo = filaProntos.get(i);
                System.out.println((i + 1) + ". " + processo); // Exibe em formato enumerado
            }
        }
    }
    

    private static void executarRoundRobin() {
        System.out.println("\nIniciando execução com Round Robin...");
        int tempoTotal = 0;
    
        while (!filaProntos.isEmpty()) {
            Iterator<Processo> iterator = filaProntos.iterator();
    
            while (iterator.hasNext()) {
                Processo processo = iterator.next();
                System.out.println("Executando processo: " + processo.nome);
    
                // Executa o processo pelo tempo do quantum ou até terminar
                int tempoExecutado = Math.min(quantum, processo.tempoRestante);
                processo.tempoRestante -= tempoExecutado;
                tempoTotal += tempoExecutado;
    
                // Incrementa o tempo de espera para outros processos
                for (Processo p : filaProntos) {
                    if (p != processo) {
                        p.tempoEspera += tempoExecutado;
                    }
                }
    
                // Verifica se o processo terminou
                if (processo.tempoRestante <= 0) {
                    processo.tempoTurnaround = tempoTotal;
                    filaFinalizados.add(processo);
                    iterator.remove(); // Remove o processo da fila de prontos após ser finalizado
                    System.out.println("Processo " + processo.nome + " finalizado.");
                } else {
                    // Se o processo não terminou, ele volta para o final da fila
                    System.out.println("Processo " + processo.nome + " preemptado.");
                }
            }
        }
    
        calcularMetricas();
    }    
    
    private static void executarPrioridadePreemptiva() {
        System.out.println("\nIniciando execução com Prioridade Preemptiva...");
        int tempoTotal = 0;

        while (!filaProntos.isEmpty()) {
            // Ordena a fila pela prioridade (menor valor = mais prioritário)
            filaProntos.sort(Comparator.comparingInt(p -> p.prioridade));
            Processo processo = filaProntos.get(0);
            System.out.println("Executando processo: " + processo.nome);

            // Executa o processo pelo tempo do quantum ou até terminar
            int tempoExecutado = Math.min(quantum, processo.tempoRestante);
            processo.tempoRestante -= tempoExecutado;
            tempoTotal += tempoExecutado;

            // Incrementa o tempo de espera para outros processos
            for (Processo p : filaProntos) {
                if (p != processo) {
                    p.tempoEspera += tempoExecutado;
                }
            }

            // Verifica se o processo terminou
            if (processo.tempoRestante <= 0) {
                processo.tempoTurnaround = tempoTotal;
                filaFinalizados.add(processo);
                filaProntos.remove(processo);
                System.out.println("Processo " + processo.nome + " finalizado.");
            } else {
                System.out.println("Processo " + processo.nome + " preemptado.");
            }
        }

        calcularMetricas();
    }

    private static void calcularMetricas() {
        System.out.println("\nMétricas Finais:");
        int somaTurnaround = 0;
        int somaEspera = 0;

        for (Processo processo : filaFinalizados) {
            System.out.println(processo.nome + ": Turnaround = " + processo.tempoTurnaround + ", Espera = " + processo.tempoEspera);
            somaTurnaround += processo.tempoTurnaround;
            somaEspera += processo.tempoEspera;
        }

        System.out.println("Tempo médio de Turnaround: " + (double) somaTurnaround / filaFinalizados.size());
        System.out.println("Tempo médio de Espera: " + (double) somaEspera / filaFinalizados.size());
    }
}