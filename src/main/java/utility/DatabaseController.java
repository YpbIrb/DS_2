package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DatabaseController {
    public static final String JDBC_DRIVER = "org.postgresql.Driver";
    static final String SERVER_CONNECTION_URL = "jdbc:postgresql://127.0.0.1:5432/";
    static final String USER = "postgres";
    static final String PASS = "password";


    String DB_CONNECTION_URL;
    String DB_NAME;

    private Connection connection;
    private static Logger logger = LogManager.getLogger(DatabaseController.class);


    public DatabaseController(String db_name){
        ConnectToServer();
        DB_NAME = db_name;
        DB_CONNECTION_URL = SERVER_CONNECTION_URL + DB_NAME;
        //Class.forName(JDBC_DRIVER).newInstance();
    }

    public void ConnectToServer(){
        try {

            connection = DriverManager.getConnection(SERVER_CONNECTION_URL, USER, PASS);

            if (connection != null) {
                logger.info("You successfully connected to database server now");
            } else {
                logger.info("Failed to make connection to database server");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public void ConnectToDatabase(){
        try {
            connection = DriverManager.getConnection(DB_CONNECTION_URL, USER, PASS);

            if (connection != null) {
                logger.info("You successfully connected to working database now");
            } else {
                logger.info("Failed to make connection to working database ");
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

    public boolean IsConnected(){
        return (!(connection == null));
    }

    //Пересоздает бд, и подключается к ней для дальнейшей работы
    public void ReInitializeAndConnectDatabase() throws SQLException {

        Statement statement = connection.createStatement();
        String sql;

        PreparedStatement ps = connection.prepareStatement("SELECT datname FROM pg_database WHERE datistemplate = false;");
        ResultSet rs = ps.executeQuery();

        //Проверяем, существует ли база osm
        boolean osm_db_created = false;
        while (rs.next()) {
            if (rs.getString(1).equals(DB_NAME)) {
                osm_db_created = true;
                break;
            }
        }

        //Если база существует, то нужно сначала отключить все сеантсы, использующие её, а потом удалить её, чтобы пересоздать
        if (osm_db_created){
            sql = "select pg_terminate_backend(pid) " +
                    "from pg_stat_activity " +
                    "where datname = '" + DB_NAME + "';";

            statement.executeQuery(sql);

            sql = "DROP DATABASE OSM";
            statement.executeUpdate(sql);
        }

        sql = "CREATE DATABASE OSM";
        statement.executeUpdate(sql);

        logger.info("Database ready");

        ConnectToDatabase();

    }

    public void CreateTables() throws SQLException {

        String sql;
        Statement statement = connection.createStatement();

        logger.info("In create tables" + connection.getCatalog());

        sql = "CREATE TABLE Node (\n" +
                "id int NOT NULL,\n" +
                "lat float NOT NULL,\n" +
                "lon float NOT NULL,\n" +
                "user_name varchar NOT NULL,\n" +
                "uid int NOT NULL,\n" +
                "version int NOT NULL,\n" +
                "changeset int NOT NULL,\n" +
                "timestamp timestamp NOT NULL,\n" +
                "PRIMARY KEY (id)\n" + ");";

        statement.executeUpdate(sql);

        logger.info("Node Table created");


        sql = "CREATE TABLE Tag (\n" +
                "node_id INT NOT NULL,\n" +
                "k varchar NOT NULL,\n" +
                "v varchar NOT NULL,\n" +
                "PRIMARY KEY (node_id, k),\n" +
                "FOREIGN KEY (node_id) REFERENCES Node (id)\n" + ");";


        statement.executeUpdate(sql);

        logger.info("Tag Table created");
    }

    public Connection getConnection() {
        return connection;
    }
}
