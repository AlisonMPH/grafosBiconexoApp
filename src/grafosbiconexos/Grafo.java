package grafosbiconexos;

/*******************************************************************************
 *  Compilação:        javac Grafo.java
 *  Execução:          java Grafo dados.txt
 *  Dependências:      Aresta.java
 *  Arquivos de dados: Grafo1.txt
 *  Link dos dados:    https://drive.google.com/open?id=0B3q56TwNCeXoMlQ1c1dGOXJRbG8
 *
 *  Um grafo de arestas, implementado utilizando listas de adjacências.
 *
 *  % java Grafo Grafo1.txt
 *  13 13
 *  0: 6  2  1  5  
 *  1: 0  
 *  2: 0  
 *  3: 5  4  
 *  4: 5  6  3  
 *  5: 3  4  0  
 *  6: 0  4  
 *  7: 8  
 *  8: 7  
 *  9: 11  10  12  
 *  10: 9  
 *  11: 9  12  
 *  12: 11  9
 *
 ******************************************************************************/


import java.util.ArrayList;
import java.util.List;


public class Grafo {

    private static final String NEWLINE = System.getProperty("line.separator");

    private final int V;         // número de vértices no grafo
    private int A;               // número de arestas no grafo
    private List<Aresta>[] adj;  // adj[v1] = lista de adjacência do vértice v1


    public Grafo(int V) {
        if (V < 0) {
            throw new IllegalArgumentException("Número de vértices no grafo deve ser não negativo");
        }
        this.V = V;
        this.A = 0;
        adj = new ArrayList[V];
        for (int v = 0; v < V; v++) {
            adj[v] = new ArrayList<>();
        }
    }

    public Grafo(In in) {
        this(in.readInt());
        int E = in.readInt();
        if (E < 0) {
            throw new IllegalArgumentException("Número de arestas deve ser não negativo");
        }
        for (int i = 0; i < E; i++) {
            int v1 = in.readInt();
            int v2 = in.readInt();
            double peso = 0;
            addAresta(new Aresta(v1, v2, peso));
        }
    }

    public Grafo() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public int V() {
        return V;
    }

    public int A() {
        return A;
    }

    private void validaVertice(int v) {
        if (v < 0 || v >= V)
            throw new IndexOutOfBoundsException("vértice " + v + " não está entre 0 e " + (V-1));
    }

    public void addAresta(Aresta a) {
        int v1 = a.umVertice();
        int v2 = a.outroVertice(v1);
        validaVertice(v1);
        validaVertice(v2);
        adj[v1].add(0, a);
        Aresta a2 = new Aresta(a.getV2(), a.getV1(), a.peso());
        adj[v2].add(0, a2);
        A++;
    }


    public List<Aresta> adj(int v) {
        validaVertice(v);
        return adj[v];
    }

    public int grau(int v) {
        validaVertice(v);
        return adj[v].size();
    }

    public List<Aresta> arestas() {
        List<Aresta> lista = new ArrayList<Aresta>();
        for (int v = 0; v < V; v++) {
            int selfLoops = 0;
            for (Aresta a : adj(v)) {
                if (a.outroVertice(v) > v) {
                    lista.add(a);
                    lista.add(v, a);
                }
                else if (a.outroVertice(v) == v) {
                    if (selfLoops % 2 == 0) {
                        lista.add(a);
                    }
                    selfLoops++;
                }
            }
        }
        return lista;
    }
    
    public boolean existeArestaEntre(int v1, int v2) {
        validaVertice(v1);
        validaVertice(v2);
        for (Aresta a : adj[v1]) {
            if (a.outroVertice(v1) == v2) {
                return true;
            }
        }
        return false;
    }
    
    // Obtenha a lista de todas as arestas únicas
    public List<Aresta> arestasUnicas() {
        List<Aresta> arestasUnicas = new ArrayList<>();
        for (Aresta aresta : this.arestas()) {
            boolean jaExiste = false;
            for (Aresta a : arestasUnicas) {
                if ((a.getV1() == aresta.getV1() && a.getV2() == aresta.getV2()) ||
                    (a.getV1() == aresta.getV2() && a.getV2() == aresta.getV1())) {
                    jaExiste = true;
                    break;
                }
            }
            if (!jaExiste) {
                arestasUnicas.add(aresta);
            }
        }
        return arestasUnicas;
    }
    
    public List<Aresta> arestasAdjacentes(Aresta aresta) {
        List<Aresta> arestasAdjacentes = new ArrayList<>();
        for (Aresta a : this.arestas()) {
            if ((a.getV1() == aresta.getV1() && a.getV2() == aresta.getV2()) ||
                (a.getV1() == aresta.getV2() && a.getV2() == aresta.getV1())) {
                continue; // Ignora a própria aresta
            }
            if (a.getV1() == aresta.getV1() || a.getV1() == aresta.getV2() ||
                a.getV2() == aresta.getV1() || a.getV2() == aresta.getV2()) {
                arestasAdjacentes.add(a);
            }
        }
        return arestasAdjacentes;
    }
    
    public List<Integer> getVizinhos(int v) {
        List<Integer> vizinhos = new ArrayList<>();
        for (Aresta aresta : this.adj(v)) {
            if (aresta.getV1() == v) {
                vizinhos.add(aresta.getV2());
            } else if (aresta.getV2() == v) {
                vizinhos.add(aresta.getV1());
            }
        }
        return vizinhos;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(V + " " + A + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Aresta a : adj[v]) {
                s.append(a + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }
    
    // incluido para remover arestas
    public void decrementarArestas() {
        if (this.A > 0) {
            this.A--;
        } else {
            System.out.println("Warning: Attempted to decrement number of edges below 0. No change was made.");
        }
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        Grafo G = new Grafo(in);
        System.out.println(G);

    }

}
