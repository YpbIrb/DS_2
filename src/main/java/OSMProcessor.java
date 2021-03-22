import DAO.NodeDAO;
import DAO.NodeDTO;
import DAO.TagDAO;
import DAO.TagDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

//import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import osm.model.generated.Node;
import osm.model.generated.Tag;
import utility.Converter;
import utility.DatabaseController;

public class OSMProcessor {

    XMLStreamReader xmlReader;
    private static Logger logger = LogManager.getLogger(OSMProcessor.class);

    public OSMProcessor(XMLStreamReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    public void ProcessOSM(Map<String, Integer> userEditsCount, Map<String, Integer> tagNodeCount, NodeProcessor nodeProcessor) throws XMLStreamException, JAXBException, SQLException {

        XMLStreamReader xsr;
        xsr = new XsiTypeReader(xmlReader);


        JAXBContext jc = JAXBContext.newInstance(Node.class);
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        System.out.println("Done");


        xsr.nextTag();
        while (! xsr.getLocalName().equals("node"))
            xsr.nextTag();                               //skip all elements before nodes

        int n = 0;
        int last_logged_node_num = 0;
        Object object;
        Node node;

        while(xsr.getLocalName().equals("node") && xsr.getEventType() == XMLStreamConstants.START_ELEMENT){
            object = unmarshaller.unmarshal(xsr);
            node =  (Node)object;

            //ProcessNodeLocally(node, userEditsCount, tagNodeCount);
            n++;

            if (n == (last_logged_node_num + 1000000)){
                last_logged_node_num = n;
                logger.info("Done with " + last_logged_node_num + " node");
            }

            nodeProcessor.ProcessNodeStatement(Converter.NodeToDTO(node));
            //nodeProcessor.ProcessNodePreparedStatement(Converter.NodeToDTO(node));
            //nodeProcessor.ProcessNodeBatch(Converter.NodeToDTO(node));

            xsr.nextTag();
        }



        //if(!nodeProcessor.IsBatchEmpty())
        //    nodeProcessor.FlushBatch();

        double elapsedTimeInSecond = (double) nodeProcessor.getTotal_time() / 1000000000;
        logger.info("Total time : " + elapsedTimeInSecond + " seconds");
        logger.info("Finish on " + last_logged_node_num + " node");
    }

    void ProcessNodeLocally(Node node, Map<String, Integer> userEditsCount, Map<String, Integer> tagNodeCount) {

        if(userEditsCount.containsKey(node.getUser())){
            userEditsCount.replace(node.getUser(), userEditsCount.get(node.getUser()) + 1);
        }
        else {
            userEditsCount.put(node.getUser(), 1);
        }

        for (Tag tag: node.getTag()) {
            ProcessTagLocally(tag, tagNodeCount);
        }

    }

    void ProcessTagLocally(Tag tag, Map<String, Integer> tagNodeCount) {
        String tagKey = tag.getK();
        //Согласно вики по OpenStreetMap https://wiki.openstreetmap.org/wiki/Node у одной Node не может быть двух тегов с одинаковыми ключами
        if(tagNodeCount.containsKey(tagKey)){
            tagNodeCount.replace(tagKey, tagNodeCount.get(tagKey) + 1);
        }
        else {
            tagNodeCount.put(tagKey, 1);
        }
    }

    //Кастомный ридер для того, чтобы можно было парсить xml, в которой пропущены пространства имен
    private static class XsiTypeReader extends StreamReaderDelegate {

        public XsiTypeReader(XMLStreamReader reader) {
            super(reader);
        }

        @Override
        public String getNamespacePrefix(int arg0) {
            return "http://openstreetmap.org/osm/0.6";
        }

        @Override
        public String getNamespaceURI(int arg0) {
            return "http://openstreetmap.org/osm/0.6";
        }

        @Override
        public String getNamespaceURI() {
            return "http://openstreetmap.org/osm/0.6";
        }


        @Override
        public String getNamespaceURI(String prefix) {
            return "http://openstreetmap.org/osm/0.6";
        }

    }


}
