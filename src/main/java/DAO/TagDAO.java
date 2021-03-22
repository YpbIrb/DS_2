package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class TagDAO {

    private PreparedStatement InsertTagPreparedStatement;
    private PreparedStatement InsertBatchTagPreparedStatement;
    private Connection connection;

    public TagDAO(Connection connection) throws SQLException {

        this.connection = connection;
        InsertTagPreparedStatement = connection.prepareStatement(
                "INSERT INTO TAG(node_id, k, v) \n" +
                        "VALUES (?, ?, ?)");

        InsertBatchTagPreparedStatement = connection.prepareStatement(
                "INSERT INTO TAG(node_id, k, v) \n" +
                        "VALUES (?, ?, ?)");


    }

    public void InsertTag(TagDTO tagDTO) throws SQLException {
        String sql = "INSERT INTO TAG(node_id, k, v) \n" +
                "VALUES (" + tagDTO.getNode_id() + ", '" +
                tagDTO.getK().replaceAll("'", "''") + "' , '" +
                tagDTO.getV().replaceAll("'", "''") + "' )";


        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);

    }

    public void InsertPreparedStatementTag(TagDTO tagDTO) throws SQLException {
        InsertTagPreparedStatement.setLong(1, tagDTO.getNode_id());
        InsertTagPreparedStatement.setString(2, tagDTO.getK());
        InsertTagPreparedStatement.setString(3, tagDTO.getV());

        InsertTagPreparedStatement.execute();
    }


    public void AddTagToBatchPreparedStatement(TagDTO tagDTO) throws SQLException {
        PrepareStatement(InsertBatchTagPreparedStatement, tagDTO);
        InsertBatchTagPreparedStatement.addBatch();
    }


    public void InsertBatchPreparedStatementTag(List<TagDTO> tags) throws SQLException {
        for (TagDTO t : tags){
            AddTagToBatchPreparedStatement(t);
        }

        InsertBatchTagPreparedStatement.executeBatch();
    }

    private void PrepareStatement(PreparedStatement preparedStatement, TagDTO tagDTO) throws SQLException {
        preparedStatement.setLong(1, tagDTO.getNode_id());
        preparedStatement.setString(2, tagDTO.getK());
        preparedStatement.setString(3, tagDTO.getV());
    }



}
