package javafxapplication2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import grafosbiconexos.Grafo;
import grafosbiconexos.Aresta;
import grafosbiconexos.In;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GrafoBiconexoApp extends Application {

    private static final int RAIO = 200; // Raio do círculo onde os vértices serão posicionados
    private static final int CENTRO_X = 400; // Centro do círculo no eixo X
    private static final int CENTRO_Y = 300; // Centro do círculo no eixo Y
    private static final int LARGURA_IMAGEM = 100; // Largura desejada da imagem
    private static final int ALTURA_IMAGEM = 100; // Altura desejada da imagem

    private Grafo grafo;
    private Pane root;

    @Override
    public void start(Stage stage) {
        root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        // Desenhar o grafo
        desenharGrafo();

        stage.setTitle("Grafo Biconexo");
        stage.setScene(scene);
        stage.show();
    }

private void desenharGrafo() {
    // Desenhando as arestas primeiro
    for (int i = 0; i < grafo.V(); i++) {
        double angulo = 2 * Math.PI * i / grafo.V();
        Point p1 = calculatePoint(i);

        for (Aresta a : grafo.adj(i)) {
            int w = a.outroVertice(i);
            Point p2 = calculatePoint(w);

            Line linha = new Line(p1.x, p1.y, p2.x, p2.y);
            linha.setStrokeWidth(8.0); // Espessura mais realista da linha
            
            final int finalI = i; // Final variable for lambda
            final int finalW = w; // Final variable for lambda
            linha.setOnMouseClicked(e -> handleArestaClick(finalI, finalW));
            root.getChildren().add(linha);
        }
    }
        // Agora desenhamos os vértices
        for (int i = 0; i < grafo.V(); i++) {
            Point p = calculatePoint(i);

            Image imagemVertice = new Image(getClass().getResourceAsStream("/resources/switch2.png"));
            ImageView viewVertice = new ImageView(imagemVertice);
            viewVertice.setFitWidth(LARGURA_IMAGEM);
            viewVertice.setFitHeight(ALTURA_IMAGEM);

            viewVertice.setX(p.x - LARGURA_IMAGEM / 2);
            viewVertice.setY(p.y - ALTURA_IMAGEM / 2);
            root.getChildren().add(viewVertice);
        }
    }

    private Point calculatePoint(int vertexIndex) {
        double angulo = 2 * Math.PI * vertexIndex / grafo.V();
        int x = CENTRO_X + (int) (RAIO * Math.cos(angulo));
        int y = CENTRO_Y + (int) (RAIO * Math.sin(angulo));
        return new Point(x, y);
    }

    // Classe interna simples para representar um ponto 2D
    private static class Point {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private void handleArestaClick(int v1, int v2) {
        removerAresta(v1, v2);
        atualizarGrafo();
    }

    private void atualizarGrafo() {
        root.getChildren().clear();
        desenharGrafo();
    }

    public void removerAresta(int v1, int v2) {
        validaVertice(v1);
        validaVertice(v2);

        removePrimeiraOcorrencia(v1, v2);
        removePrimeiraOcorrencia(v2, v1);

        // Supondo que a classe Grafo tenha um método para decrementar o número de arestas
        grafo.decrementarArestas();
    }

    private void removePrimeiraOcorrencia(int origem, int destino) {
        List<Aresta> listaAdjacencia = grafo.adj(origem);
        for (int i = 0; i < listaAdjacencia.size(); i++) {
            if (listaAdjacencia.get(i).outroVertice(origem) == destino) {
                listaAdjacencia.remove(i);
                break;
            }
        }
    }
    
    private void validaVertice(int v) {
        if (v < 0 || v >= grafo.V()) {
            throw new IllegalArgumentException("Vértice " + v + " é inválido");
        }
    }

   @Override
public void init() {
    try {
        // Carrega o grafo a partir de uma URL usando caminho relativo
        URL urlGrafo = getClass().getResource("/resources/Grafo1.txt");
        if (urlGrafo == null) {
            throw new IllegalArgumentException("O arquivo do grafo não foi encontrado.");
        }
        In in = new In(urlGrafo); // Cria um objeto In com a URL
        grafo = new Grafo(in); // Usa o objeto In para criar o grafo
    } catch (Exception e) {
        e.printStackTrace();
        // Tratar a exceção adequadamente
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}
