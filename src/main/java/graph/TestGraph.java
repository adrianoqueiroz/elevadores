package graph;

import java.util.ArrayList;
import java.util.Collection;
import model.Elevador;
import model.Usuario;

public class TestGraph {

    public static void main(String... args) throws VertexException {
        /*
        Collection<Elevador> elevadorCollection = new ArrayList<>();

        Elevador e1 = new Elevador();
        Elevador e2 = new Elevador();

        Collection<Integer> origemCollection = new ArrayList<>();
        origemCollection.add(1);
        origemCollection.add(4);
        origemCollection.add(7);

        Collection<Integer> destinoCollection = new ArrayList<>();
        destinoCollection.add(2);
        destinoCollection.add(5);
        destinoCollection.add(8);

        e1.setAndar(5);
        e1.setEstado("Subindo");
        e1.setObjetivo("Sobe");
        e1.setParadasOrigem(origemCollection);
        e1.setParadasDestino(destinoCollection);

        e1.setAndar(8);
        e1.setEstado("Parado");
        e1.setObjetivo("Nenhum");

        elevadorCollection.add(e1);
        elevadorCollection.add(e2);

        Usuario u = new Usuario();

        u.setAndarOrigem(2);
        u.setAndarDestino(7);

        Graph g = createGraphElevadores(elevadorCollection, u);

        printVertices(g);

        printEdges(g);

        Vertex v = g.getVertex(0);
        
        GraphUtil.dethFirstSearch(v);

        GraphUtil.resetStatus(g);

        GraphUtil.breathFirstSearch(v);

        GraphUtil.resetStatus(g);

        GraphUtil.dijkstra(v);
        GraphUtil.printShortestPathTo(g);

        /*
         Graph g = createGraph();
        

         printVertices(g);

         printEdges(g);

         Vertex v = g.getVertex(0);
         GraphUtil.dethFirstSearch(v);

         GraphUtil.resetStatus(g);

         GraphUtil.breathFirstSearch(v);

         GraphUtil.resetStatus(g);

         GraphUtil.dijkstra(v);
         GraphUtil.printShortestPathTo(g);
         */
    }

    private static void printEdges(Graph g) {
        System.out.println("Edges = " + g.numEdges());
        for (Edge edge : g.edges()) {
            System.out.println("Edge = " + edge.getWeigth() + " "
                    + edge.getV1() + " - " + edge.getV2());
        }
        System.out.println();
    }

    private static void printVertices(Graph g) {
        System.out.println("Vertices = " + g.numVertices());
        for (Vertex vertex : g.vertices()) {
            System.out.print("Vertex:" + vertex.getId() + "-"
                    + vertex.getName());
            System.out.print(" - Adjacents");
            for (Vertex va : vertex.getAdjacents()) {
                System.out.print(" - " + va.getId() + ":" + va.getName());
            }
            System.out.println();
        }
    }

    private static Graph createGraph() throws VertexException {
        Graph g = new Graph();
        Vertex v0 = g.insertVertex(0, "V0");
        Vertex v1 = g.insertVertex(1, "V1");
        Vertex v2 = g.insertVertex(2, "V2");
        Vertex v3 = g.insertVertex(3, "V3");
        Vertex v4 = g.insertVertex(4, "V4");
        Vertex v5 = g.insertVertex(5, "V5");
        Vertex v6 = g.insertVertex(6, "V6");
        g.insertEdge(v0, v1, 2);
        g.insertEdge(v0, v2, 3);
        g.insertEdge(v0, v3, 4);
        g.insertEdge(v1, v4, 7);
        g.insertEdge(v1, v2, 1);
        g.insertEdge(v2, v5, 9);
        g.insertEdge(v2, v3, 2);
        g.insertEdge(v3, v6, 1);
        g.insertEdge(v4, v5, 2);
        g.insertEdge(v5, v6, 2);

        return g;
    }

    private static Graph createGraphElevadores(Collection<Elevador> elevadorCollection, Usuario usuario) throws VertexException {
        Graph g = new Graph();

        //andar do usuario
        Vertex andarOrigem = g.insertVertex(usuario.getAndarOrigem(), usuario.getAndarOrigem() + "º Andar");

        //insere todos edges
        for (Elevador elevador : elevadorCollection) {
            //se o elevador estiver parado
            Collection<Integer> todasParadas = elevador.getTodasParadas();
            int custoInicial = 0;

            //calculo do custo inicial
            if (null != elevador.getObjetivo()) {
                switch (elevador.getObjetivo()) {
                    case "Nenhum":
                        custoInicial = 1;
                        break;
                    case "Subir":
                        //quantidade de paradas até o andar de origem do usuario
                        if (usuario.getAndarOrigem() >= elevador.getAndar()) {
                            custoInicial = 1;
                            for (int i : todasParadas) {
                                if (todasParadas.contains(i) && i < usuario.getAndarOrigem()) {
                                    custoInicial++;
                                }
                            }
                        }
                        break;
                    case "Descer":
                        //quantidade de paradas até o andar de origem do usuario
                        if (usuario.getAndarOrigem() <= elevador.getAndar()) {
                            custoInicial = 1;
                            for (int i : todasParadas) {
                                if (todasParadas.contains(i) && i > usuario.getAndarOrigem()) {
                                    custoInicial++;
                                }
                            }

                        }
                        break;
                }
            }

            //proximos vertices que o usuario irá passar se usar esse elevador
            //subindo
            boolean primeiraVez = true;
            if (usuario.getAndarOrigem() < usuario.getAndarDestino()) {
                Vertex vertexAnterior = andarOrigem;
                for (int i : todasParadas) {
                    if (i > usuario.getAndarOrigem()) {
                        Vertex novoVertex = g.insertVertex(i, i + "º Andar");
                        if (primeiraVez) {
                            g.insertEdge(andarOrigem, novoVertex, custoInicial + 1);
                            primeiraVez = false;
                        } else {
                            g.insertEdge(vertexAnterior, novoVertex, 1);
                        }
                        vertexAnterior = novoVertex;
                    }
                }
                Vertex ultimoVertex = g.insertVertex(usuario.getAndarDestino(), usuario.getAndarDestino() + "º Andar");
                g.insertEdge(vertexAnterior, ultimoVertex, 1);
            } //descendo
            else if (usuario.getAndarOrigem() > usuario.getAndarDestino()) {
                Vertex vertexAnterior = andarOrigem;
                for (int i : todasParadas) {
                    if (i < usuario.getAndarOrigem()) {
                        Vertex novoVertex = g.insertVertex(i, i + "º Andar");
                        if (primeiraVez) {
                            g.insertEdge(andarOrigem, novoVertex, custoInicial + 1);
                            primeiraVez = false;
                        } else {
                            g.insertEdge(vertexAnterior, novoVertex, 1);
                        }
                        vertexAnterior = novoVertex;
                    }
                }
                Vertex ultimoVertex = g.insertVertex(usuario.getAndarDestino(), usuario.getAndarDestino() + "º Andar");
                g.insertEdge(vertexAnterior, ultimoVertex, 1);

            }
           
        }
        return g;
    }
}
