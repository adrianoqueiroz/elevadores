package graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex implements Comparable<Vertex> {

    private List<Vertex> adjacents = new ArrayList<Vertex>();

    private List<Edge> edges = new ArrayList<Edge>();

    private Integer id;

    private String name;

    private int dist = Integer.MAX_VALUE;

    private Vertex previous;

    private String status = GraphUtil.UNVISITED;

    public Vertex(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getDegree() {
        return this.adjacents.size();
    }

    public List<Vertex> getAdjacents() {
        return adjacents;
    }

    public void setAdjacents(List<Vertex> adjacents) {
        this.adjacents = adjacents;
    }

    public void addAdjacents(Vertex adjacents) {
        this.adjacents.add(adjacents);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Edge insertEdge(Vertex w, Integer o) {
        Edge e = new Edge();
        e.setV1(this);
        e.setV2(w);
        e.setWeigth(o);
        this.edges.add(e);
        return e;
    }

    public Edge insertEdge(Vertex w, Edge e, Integer weigth) {
        e.setV1(w);
        e.setV2(this);
        e.setWeigth(weigth);
        this.edges.add(e);
        return e;
    }

    public void removeAllEdges() {
        this.edges = new ArrayList<Edge>();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vertex) {
            if (this.id.equals(((Vertex) obj).getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "(Vertex ID:" + id + "-" + name + ", degree:" + getDegree()
                + ")";
    }

    public int compareTo(Vertex other) {
        return Double.compare(dist, other.getDist());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}






