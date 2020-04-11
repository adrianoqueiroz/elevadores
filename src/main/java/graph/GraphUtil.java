package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;


import java.util.Queue;

public class GraphUtil {

    public static String VISITED = "VISITED";

    public static String UNVISITED = "UNVISITED";

    public static String RETURN = "RETURN";

    public static String CROSS = "CROSS";

    public static List<String> stack = new ArrayList<String>();

    private static boolean go = true;

    private static boolean back = true;

    public static void dethFirstSearch(Vertex vertex) throws VertexException {
        System.out.println("\nDETH FIRST SEARCH on GRAPH");
        System.out.print("START V:" + vertex.getId() + "-" + vertex.getName());
        vertex.setStatus(VISITED);
        executeDethFirstSearch(vertex);
    }

    public static void executeDethFirstSearch(Vertex vertex)
            throws VertexException {

        for (Edge edge : vertex.getEdges()) {
            if (edge.getStatus().equals(UNVISITED)) {
                Vertex w = Graph.opposite(vertex, edge);
                if (w.getStatus().equals(UNVISITED)) {
                    edge.setStatus(VISITED);
                    w.setStatus(VISITED);
                    if (go) {
                        System.out.println();
                        go = false;
                    }
                    System.out.print("-> GO E:" + edge.getWeigth() + " V:"
                            + w.getId() + "-" + w.getName() + " ");
                    back = true;
                    executeDethFirstSearch(w);
                    if (back) {
                        System.out.println();
                        back = false;
                    }
                    System.out.print("-> BACK E:" + edge.getWeigth() + " V:"
                            + vertex.getId() + "-" + vertex.getName() + " ");
                    go = true;
                }
            }
        }
    }

    public static void breathFirstSearch(Vertex vertex) throws VertexException {
        System.out.println("\n\nBREATH FIRST SEARCH on GRAPH");
        Queue<Vertex> queue = new LinkedList<Vertex>();
        queue.add(vertex);
        System.out
                .print("START = V:" + vertex.getId() + "-" + vertex.getName());
        while (!queue.isEmpty()) {
            Vertex v = queue.remove();
            System.out.print("\nBACK = V:" + v.getId() + "-" + v.getName());
            for (Edge e : v.getEdges()) {
                if (e.getStatus().equals(UNVISITED)) {
                    Vertex w = Graph.opposite(v, e);
                    if (w.getStatus().equals(UNVISITED)) {
                        e.setStatus(VISITED);
                        w.setStatus(VISITED);
                        queue.add(w);
                        System.out.print(" -> GO = E:" + e.getWeigth() + " V:"
                                + w.getId() + "-" + w.getName());
                    }
                }
            }
        }
        System.out.println();
    }

    public static void resetStatus(Graph graph) {
        for (int i = 0; i < graph.edges().size(); i++) {
            graph.edges().get(i).setStatus(UNVISITED);
        }
        for (int i = 0; i < graph.vertices().size(); i++) {
            graph.vertices().get(i).setStatus(UNVISITED);
        }
    }

    public static void dijkstra(Vertex init) throws VertexException {
        System.out.println("\n\nDIJKSTRA on GRAPH");

        init.setDist(0);
        PriorityQueue<Vertex> queue = new PriorityQueue<Vertex>();
        queue.add(init);

        while (!queue.isEmpty()) {
            Vertex u = queue.poll();

            for (Edge e : u.getEdges()) {
                Vertex v = Graph.opposite(u, e);
                int weight = e.getWeigth();
                int distanceThroughU = u.getDist() + weight;
                if (distanceThroughU < v.getDist()) {
                    queue.remove(v);
                    v.setDist(distanceThroughU);
                    v.setPrevious(u);
                    queue.add(v);
                }
            }
        }
    }

    public static List<Vertex> getShortestPathTo(Vertex target) {
        List<Vertex> path = new ArrayList<Vertex>();
        for (Vertex vertex = target; vertex != null; vertex = vertex
                .getPrevious()) {
            path.add(vertex);
        }
        Collections.reverse(path);
        return path;
    }

    public static void printShortestPathTo(Graph graph) {
        System.out.println("PRINT SHORTEST PATH");
        for (Vertex v : graph.vertices()) {
            System.out.println("Distance to " + v.getName() + ": "
                    + v.getDist());
            List<Vertex> path = getShortestPathTo(v);
            System.out.println("Path: " + path);
        }
    }
}
