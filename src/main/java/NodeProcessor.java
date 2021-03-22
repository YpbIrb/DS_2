import DAO.NodeDAO;
import DAO.NodeDTO;
import DAO.TagDAO;
import DAO.TagDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class NodeProcessor {

    private final int buffer_size_max = 100000;
    private int current_buffer_size;
    private Connection connection;
    private NodeDAO nodeDAO;
    private TagDAO tagDAO;
    private List<NodeDTO> BatchBuffer;
    private long total_time;

    public NodeProcessor(Connection connection) throws SQLException {
        this.connection = connection;
        nodeDAO = new NodeDAO(connection);
        tagDAO = new TagDAO(connection);
        BatchBuffer = new ArrayList<>();
        current_buffer_size = 0;
        total_time = 0;
    }

    public void ProcessNodeStatement(NodeDTO nodeDTO) throws SQLException {

        long start = System.nanoTime();

        nodeDAO.InsertNode(nodeDTO);
        for (TagDTO tagDTO : nodeDTO.getTags()){
            tagDAO.InsertTag(tagDTO);
        }

        long finish = System.nanoTime();

        total_time += (finish - start);
    }

    public void ProcessNodePreparedStatement(NodeDTO nodeDTO) throws SQLException {
        long start = System.nanoTime();

        nodeDAO.InsertPreparedStatementNode(nodeDTO);
        for (TagDTO tagDTO : nodeDTO.getTags()){
            tagDAO.InsertPreparedStatementTag(tagDTO);
        }

        long finish = System.nanoTime();

        total_time += (finish - start);
    }

    public void ProcessNodeBatch(NodeDTO node) throws SQLException {
        long start = System.nanoTime();

        if(current_buffer_size < buffer_size_max - 1){
            current_buffer_size++;
            BatchBuffer.add(node);
        }
        else {
            BatchBuffer.add(node);
            FlushBatch();
        }

        long finish = System.nanoTime();

        total_time += (finish - start);

    }

    public void FlushBatch() throws SQLException {
        long start = System.nanoTime();
        nodeDAO.InsertBatchPreparedStatementNode(BatchBuffer);
        List<TagDTO> tags = new ArrayList<>();
        for (NodeDTO n: BatchBuffer) {
            tags.addAll(n.getTags());
        }

        tagDAO.InsertBatchPreparedStatementTag(tags);
        current_buffer_size = 0;
        BatchBuffer.clear();

        long finish = System.nanoTime();

        total_time += (finish - start);
    }

    public boolean IsBatchEmpty(){
        return BatchBuffer.isEmpty();
    }

    public long getTotal_time() {
        return total_time;
    }
}
