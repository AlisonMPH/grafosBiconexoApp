package grafosbiconexos;

import grafosbiconexos.Aresta;
import grafosbiconexos.Grafo;
import grafosbiconexos.In;

/******************************************************************************
 *  Compilação:   javac AlgoritmoBiconexo.java
 *  Execução:     java  AlgoritmoBiconexo dados.txt
 *  Dependências: Grafo.java Aresta.java
 *
 *  Identificar pontos de articulação (vértice de corte) e imprimi-los.
 *  Pode ser utilizado para decompor um grafo em vértice de corte.
 ******************************************************************************/

public class AlgoritmoBiconexo {
    private int[] low;
    private int[] pre;
    private int cont;
    private boolean[] articulacao;

    // Construtor da classe AlgoritmoBiconexo. 
// Inicializa as estruturas de dados e executa o algoritmo para encontrar pontos de articulação em um grafo G.
public AlgoritmoBiconexo(Grafo G) {
    low = new int[G.V()]; // 'low': armazena o menor índice de DFS alcançável de cada vértice.
    pre = new int[G.V()]; // 'pre': armazena a ordem em que os vértices são visitados no DFS.
    articulacao = new boolean[G.V()]; // 'articulacao': indica se um vértice é um ponto de articulação.
    
    // Inicializa os arrays 'low' e 'pre' com -1 para todos os vértices indicando que nenhum vértice foi visitado.
     for (int v = 0; v < G.V(); v++)
            low[v] = -1;
     for (int v = 0; v < G.V(); v++)
            pre[v] = -1;
    
    // Inicia uma DFS para cada vértice não visitado para encontrar pontos de articulação.
    for (int v = 0; v < G.V(); v++)
        if (pre[v] == -1) // Se o vértice ainda não foi visitado, inicia a DFS neste vértice.
            dfs(G, v, v);
}

// Método DFS privado que realiza a busca em profundidade para encontrar pontos de articulação.
private void dfs(Grafo G, int u, int v) {
    int filhos = 0; // Contador para o número de subárvores na DFS.
    pre[v] = cont++; // Define a ordem de pré-visita de 'v' e incrementa o contador global 'cont'.
    low[v] = pre[v]; // Inicialmente, define 'low[v]' como 'pre[v]'.
    
    // Explora todas as arestas adjacentes ao vértice 'v'.
    for (Aresta a : G.adj(v)) {
        int w = a.getV2(); // 'w' é o vértice na extremidade oposta da aresta 'a'.
        
        // Se 'w' ainda não foi visitado, executa DFS recursivamente em 'w'.
        if (pre[w] == -1) {
            filhos++; // Incrementa o contador de filhos para a raiz da DFS.
            dfs(G, v, w); // Chamada recursiva para a DFS.

            // Atualiza o 'low[v]' com o menor valor entre o atual 'low[v]' e 'low[w]'.
            low[v] = Math.min(low[v], low[w]);

            // Se 'v' não é a raiz da DFS e 'low[w]' é maior ou igual a 'pre[v]',
            // então 'v' é um ponto de articulação.
            if (low[w] >= pre[v] && u != v) 
                articulacao[v] = true;
        }
        // Se 'w' é diferente de 'u' (não é a aresta de retorno), atualiza 'low[v]'.
        else if (w != u)
            low[v] = Math.min(low[v], pre[w]);
    }

    // Se 'v' é a raiz da DFS e tem mais de um filho, então 'v' é um ponto de articulação.
    if (u == v && filhos > 1)
        articulacao[v] = true;
}

    // o vértice v é um ponto de articulação?
    public boolean ehArticulacao(int v) { return articulacao[v]; }

    /**
     * Testa a classe AlgoritmoBiconexo.
     */
    public static void main(String[] args) {
        // instanciando o Grafo G via arquivo
        In in = new In(args[0]);
        Grafo G = new Grafo(in);
        System.out.println(G);
        
        /*
        // instanciando o Grafo G via código
        Grafo G = new Grafo(4);
        G.addAresta(new Aresta(0, 1));
        G.addAresta(new Aresta(0, 2));
        G.addAresta(new Aresta(1, 3));
        G.addAresta(new Aresta(2, 3));
        System.out.println(G);
        */

        AlgoritmoBiconexo bic = new AlgoritmoBiconexo(G);

        // imprime pontos de articulação
        System.out.println("Pontos de Articulação");
        System.out.println("---------------------");
        for (int v = 0; v < G.V(); v++)
            if (bic.ehArticulacao(v)) System.out.println(v);
        // um grafo biconexo não possui pontos de articulação
    }


}

