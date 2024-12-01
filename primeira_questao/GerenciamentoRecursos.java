package primeira_questao;

public class GerenciamentoRecursos {

    // Recursos compartilhados
    private static int recurso1 = 0;
    private static int recurso2 = 0;

    // Objetos para sincronização de acesso aos recursos
    private static final Object lockRecurso1 = new Object();
    private static final Object lockRecurso2 = new Object();

    public static void main(String[] args) {
        // Criação de 5 threads concorrentes
        for (int i = 1; i <= 5; i++) {
            final int threadId = i; // Identificador único para cada thread
            new Thread(() -> {
                while (true) {
                    long startTime = System.currentTimeMillis(); // Início da execução da thread
                    acessarRecursos(threadId); // Cada thread tenta acessar os recursos
                    long endTime = System.currentTimeMillis(); // Fim da execução da thread
                    long timeTaken = endTime - startTime; // Calculando o tempo gasto

                    // Log do tempo de execução da thread
                    System.out.println("Thread " + threadId + " levou " + timeTaken + " ms para acessar os recursos.");
                    
                    try {
                        // Simula a permuta de threads a cada 3 segundos
                        Thread.sleep(3000); // Interrupção da thread para permuta
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread " + threadId + " foi interrompida.");
                    }
                }
            }).start();
        }
    }

    private static void acessarRecursos(int threadId) {
        try {
            // Tentando acessar Recurso 1
            System.out.println("Thread " + threadId + " aguardando para acessar Recurso 1...");
            synchronized (lockRecurso1) {
                System.out.println("Thread " + threadId + " acessou Recurso 1. Valor: " + recurso1);
                recurso1++;  // Modifica o recurso compartilhado
                Thread.sleep(1000); // Simula trabalho no recurso
                System.out.println("Thread " + threadId + " liberou Recurso 1.");
            }
            
            System.out.println("Thread " + threadId + " agora verificando Recurso 2...");
    
            // Tentando acessar Recurso 2
            System.out.println("Thread " + threadId + " aguardando para acessar Recurso 2...");
            synchronized (lockRecurso2) {
                System.out.println("Thread " + threadId + " acessou Recurso 2. Valor: " + recurso2);
                recurso2++; // Modifica o recurso compartilhado
                Thread.sleep(1000); // Simula trabalho no recurso
                System.out.println("Thread " + threadId + " liberou Recurso 2.");
            }
    
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadId + " foi interrompida enquanto aguardava.");
        }
    }    
    
}
