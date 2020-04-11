package graph;

import java.util.ArrayList;


import java.util.List;

public class Graph {

    private int numVertices = 0;
    private int numEdges = 0;
    private List<Vertex> vertices = new ArrayList<Vertex>();
    private List<Edge> edges = new ArrayList<Edge>();

    public Vertex getVertex(Integer id) {
        for (Vertex v : vertices) {
            if (v.getId().equals(id)) {
                return v;
            }
        }
        return null;
    }

    public int numVertices() {
        return numVertices;
    }

    public int numEdges() {
        return numEdges;
    }

    public List<Vertex> vertices() {
        return vertices;
    }

    public List<Edge> edges() {
        return edges;
    }

    public Vertex aVertex() {
        return null;
    }

    public int degree(Vertex v) {
        return v.getDegree();
    }

    public List<Vertex> adjacentVertices(Vertex v) {
        return v.getAdjacents();
    }

    public List<Edge> incidentEdges(Vertex v) {
        return v.getEdges();
    }

    public List<Vertex> endVertices(Edge e) {
        List<Vertex> vs = new ArrayList();
        vs.add(e.getV1());
        vs.add(e.getV2());
        return vs;
    }

    public static Vertex opposite(Vertex v, Edge e) throws VertexException {
        // retorna o ponto final da aresta e diferente de v.
        for (Edge edge : v.getEdges()) {
            if (edge.equals(e)) {
                if (v.equals(edge.getV1())) {
                    return edge.getV2();
                } else {
                    return edge.getV1();
                }
            }
        }
        throw new VertexException(VertexException.ID_NO_EXIST, null);
    }

    public boolean areAdjacent(Vertex v, Vertex w) {
        // retorna true se v e w sao adjacentes
        for (Vertex vertex : v.getAdjacents()) {
            if (vertex.equals(w)) {
                return true;
            }
        }
        return false;
    }

    public Edge insertEdge(Vertex v, Vertex w, Integer weigth) {
        Edge e = v.insertEdge(w, weigth);
        w.insertEdge(v, e, weigth);
        v.addAdjacents(w);
        w.addAdjacents(v);
        edges.add(e);
        numEdges++;
        return e;
    }

    public Vertex insertVertex(Integer id, String name) throws VertexException {
        for (Vertex vertex : vertices) {
            if (vertex.getId().equals(id)) {
                throw new VertexException(VertexException.ID_DUPLICATED, id);
            }
        }
        Vertex v = new Vertex(id, name);
        vertices.add(v);
        numVertices++;
        return v;
    }

    public void removeVertex(Vertex v) {
        // remove o vertice v e todas as suas arestas incidentes
        vertices.remove(v);
        numVertices--;
        v.removeAllEdges();
    }

    public void removeEdge(Edge e) {
        // remove a aresta e
        e.setV1(null);
        e.setV2(null);
        edges.remove(e);
    }
}
