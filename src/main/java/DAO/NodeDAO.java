package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

public class NodeDAO {

    private PreparedStatement InsertNodePreparedStatement;
    private PreparedStatement InsertBatchNodePreparedStatement;
    private Connection connection;

    public NodeDAO(Connection connection) throws SQLException {
        this.connection = connection;
        InsertNodePreparedStatement = connection.prepareStatement(
                "INSERT INTO NODE(id, lat, lon, user_name, uid, version, changeset, timestamp)\n" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

        InsertBatchNodePreparedStatement = connection.prepareStatement(
                "INSERT INTO NODE(id, lat, lon, user_name, uid, version, changeset, timestamp)\n" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");

    }

    public void InsertNode(NodeDTO nodeDTO) throws SQLException {
        String sql = "INSERT INTO NODE(id, lat, lon, user_name, uid, version, changeset, timestamp) \n" +
                "VALUES (" + nodeDTO.getId() + ", " +
                nodeDTO.getLat() + ", " +
                nodeDTO.getLon() + ", '" +
                nodeDTO.getUser_name().replaceAll("'", "''") + "' , " +
                nodeDTO.getUid() + ", " +
                nodeDTO.getVersion() + ", " +
                nodeDTO.getChangeset() + ", '" +
                nodeDTO.getTimestomp() + "' )";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }


    public void InsertPreparedStatementNode(NodeDTO nodeDTO) throws SQLException {
        PrepareStatement(InsertNodePreparedStatement, nodeDTO);
        InsertNodePreparedStatement.execute();
    }

    public void AddNodeToBatchPreparedStatement(NodeDTO nodeDTO) throws SQLException {
        PrepareStatement(InsertBatchNodePreparedStatement, nodeDTO);
        InsertBatchNodePreparedStatement.addBatch();
    }

    public void InsertBatchPreparedStatementNode(List<NodeDTO> nodes) throws SQLException {

        for (NodeDTO n : nodes){
            AddNodeToBatchPreparedStatement(n);
        }

        InsertBatchNodePreparedStatement.executeBatch();
    }

    private void PrepareStatement(PreparedStatement preparedStatement, NodeDTO nodeDTO) throws SQLException {
        preparedStatement.setLong(1, nodeDTO.getId());
        preparedStatement.setDouble(2, nodeDTO.getLat());
        preparedStatement.setDouble(3, nodeDTO.getLon());
        preparedStatement.setString(4, nodeDTO.getUser_name());
        preparedStatement.setLong(5, nodeDTO.getUid());
        preparedStatement.setInt(6, nodeDTO.getVersion());
        preparedStatement.setLong(7, nodeDTO.getChangeset());
        preparedStatement.setDate(8, nodeDTO.getTimestomp());
    }


}
