package graph;

public class VertexException extends Exception {

    private static final long serialVersionUID = 4195477243645994918L;

    public static String ID_DUPLICATED = "ID_DUPLICATED";

    public static String ID_NO_EXIST = "ID_NO_EXIST";

    public VertexException(String ex, Object value) {
        if (ID_DUPLICATED.equals(ex)) {
            System.out.println("Error: ID Vertex " + value
                    + " duplicated Exception.");
        } else if (ID_NO_EXIST.equals(ex)) {
            System.out.println("Error: ID no exist for Vertex Exception.");
        } else {
            System.out.println("Error: Vertex Exception");
        }
    }
}
