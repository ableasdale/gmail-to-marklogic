import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Map;

import org.apache.tika.detect.TypeDetector;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.RecursiveParserWrapper;
import org.apache.tika.parser.mbox.MboxParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;


public class TikaMboxReader {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static InputStream getStream(String name) {
        return TikaMboxReader.class.getClass().getResourceAsStream(name);
    }

    public static void main(String[] args) {

        ParseContext recursingContext;
        Parser autoDetectParser;
        TypeDetector typeDetector;
        MboxParser mboxParser;


            typeDetector = new TypeDetector();
            autoDetectParser = new AutoDetectParser(typeDetector);
            recursingContext = new ParseContext();
            recursingContext.set(Parser.class, autoDetectParser);

            mboxParser = new MboxParser();
            mboxParser.setTracking(true);

            ContentHandler handler = new BodyContentHandler(-1);
            Metadata metadata = new Metadata();

            try (InputStream stream = new FileInputStream("/Users/ableasdale/Downloads/smallmbox.mbox.txt")) {
                mboxParser.parse(stream, handler, metadata, recursingContext);
                Map<Integer, Metadata> mailsMetadata = mboxParser.getTrackingMetadata();
//                for (Object o :mboxParser.getTrackingMetadata().entrySet()){
//                    (Metadata) o.
//                }
                LOG.info("Map size"+mailsMetadata.size());



            } catch (IOException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
    }
}
