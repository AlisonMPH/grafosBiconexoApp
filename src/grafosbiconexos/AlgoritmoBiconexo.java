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

    public AlgoritmoBiconexo(Grafo G) {
        low = new int[G.V()];
        pre = new int[G.V()];
        articulacao = new boolean[G.V()];
        for (int v = 0; v < G.V(); v++)
            low[v] = -1;
        for (int v = 0; v < G.V(); v++)
            pre[v] = -1;
        
        for (int v = 0; v < G.V(); v++)
            if (pre[v] == -1)
                dfs(G, v, v);
    }

    private void dfs(Grafo G, int u, int v) {
        int filhos = 0;
        pre[v] = cont++;
        low[v] = pre[v];
        for (Aresta a : G.adj(v)) {
            int w = a.getV2();
            if (pre[w] == -1) {
                filhos++;
                dfs(G, v, w);

                // atualiza números low (baixo)
                low[v] = Math.min(low[v], low[w]);

                // não-raíz de DFS é um ponto de articulação if low[w] >= pre[v]
                if (low[w] >= pre[v] && u != v) 
                    articulacao[v] = true;
            }

            // atualiza número low - ignora o reverso da aresta que leva a v
            else if (w != u)
                low[v] = Math.min(low[v], pre[w]);
        }

        // raíz de DFS é um ponto de articulação se este tem mais do que 1 filho
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

