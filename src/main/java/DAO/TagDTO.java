package DAO;

public class TagDTO {
    private long node_id;
    private String k;
    private String v;

    public TagDTO(long node_id, String k, String v) {
        this.node_id = node_id;
        this.k = k;
        this.v = v;
    }

    public long getNode_id() {
        return node_id;
    }

    public String getK() {
        return k;
    }

    public String getV() {
        return v;
    }
}
