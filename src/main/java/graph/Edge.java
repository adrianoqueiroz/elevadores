package graph;

public class Edge {

    private Vertex v1;

    private Vertex v2;

    private Integer weigth;

    private String status = GraphUtil.UNVISITED;

    public Vertex getV1() {
        return v1;
    }

    public void setV1(Vertex v1) {
        this.v1 = v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public void setV2(Vertex v2) {
        this.v2 = v2;
    }

    public Integer getWeigth() {
        return weigth;
    }

    public void setWeigth(Integer weigth) {
        this.weigth = weigth;
    }

    @Override
    public String toString() {
        return "W = " + weigth;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}