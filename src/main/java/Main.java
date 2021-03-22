import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import utility.DatabaseController;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.sql.SQLException;
import java.util.Comparator;

import java.util.HashMap;
import java.util.Map;


public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        File bz2File = new File("RU-NVS.osm.bz2");


            try (InputStream  bzIn = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(bz2File)))){

                Class.forName(DatabaseController.JDBC_DRIVER);
                DatabaseController databaseController = new DatabaseController("osm");


                databaseController.ReInitializeAndConnectDatabase();
                databaseController.CreateTables();

                XMLInputFactory factory = XMLInputFactory.newFactory();
                XMLStreamReader xmlReader = factory.createXMLStreamReader(bzIn);
                Map<String, Integer> UserEditsCount = new HashMap<>();
                Map<String, Integer> TagNodeCount = new HashMap<>();

                OSMProcessor osmProcessor = new OSMProcessor(xmlReader);
                NodeProcessor nodeProcessor = new NodeProcessor(databaseController.getConnection());
                osmProcessor.ProcessOSM(UserEditsCount, TagNodeCount, nodeProcessor);


        } catch (SQLException | IOException | ClassNotFoundException | XMLStreamException | JAXBException e) {
            e.printStackTrace();
        }


    }
}
