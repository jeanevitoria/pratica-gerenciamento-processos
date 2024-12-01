package segunda_questao;

class Processo {
    int id;
    String nome;
    int prioridade;
    boolean ioBound;
    int tempoTotalCPU;
    int tempoRestante;
    int tempoEspera = 0;
    int tempoTurnaround = 0;

    Processo(int id, String nome, int prioridade, boolean ioBound, int tempoTotalCPU) {
        this.id = id;
        this.nome = nome;
        this.prioridade = prioridade;
        this.ioBound = ioBound;
        this.tempoTotalCPU = tempoTotalCPU;
        this.tempoRestante = tempoTotalCPU;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Nome: " + nome +
                ", Prioridade: " + prioridade +
                ", Tipo: " + (ioBound ? "I/O-bound" : "CPU-bound") +
                ", Tempo Restante: " + tempoRestante;
    }

}