import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.zstandard.ZstdCompressorOutputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.*;
import javax.xml.stream.util.StreamReaderDelegate;
import java.io.*;
import java.util.Comparator;
import javax.xml.bind.JAXBContext;

import java.util.HashMap;
import java.util.Map;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {




        System.out.println("Hello world");
        logger.info("In Hello world");


        File bz2File = new File("RU-NVS.osm.bz2");
        File outFile = new File("result.txt");


        try (InputStream  bzIn = new BZip2CompressorInputStream(new BufferedInputStream(new FileInputStream(bz2File)))){

            if (outFile.createNewFile()) {
                logger.info("Output file was created");
            } else {
                logger.info("Output file already exist. Rewriting it.");
            }

            PrintWriter pw = new PrintWriter(outFile);
            XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader xmlReader = factory.createXMLStreamReader(bzIn);
            Map<String, Integer> UserEditsCount = new HashMap<>();
            Map<String, Integer> TagNodeCount = new HashMap<>();

            OSMProcessor osmProcessor = new OSMProcessor(xmlReader);
            osmProcessor.ProcessOSM(UserEditsCount, TagNodeCount);

            logger.info("Starting writing result into file");
            WriteResults(UserEditsCount, TagNodeCount, pw);
            logger.info("writing done");

        }  catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
        } catch (JAXBException e) {
            e.printStackTrace();
        }


    }


    private static void WriteResults(Map<String, Integer> userEditsCount, Map<String, Integer> tagNodeCount, PrintWriter pw){

        pw.println("User's node edits count : " +
                    "\n{");
        userEditsCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> pw.println(e.getKey() + " : " + e.getValue()));
        pw.println("}");

        pw.println("Nodes with tag count :" +
                "\n{");
        tagNodeCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(e -> pw.println(e.getKey() + " : " + e.getValue()));
        pw.println("}");

        pw.flush();
    }

}
