import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.MimeConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;


public class MyOwnParser {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static int count = 0;
    static StringBuilder sb = new StringBuilder();
    static ContentSource cs;


    public static void main(String[] args) {

        try {
            cs = ContentSourceFactory.newContentSource(URI.create("xcc://q:q@localhost:8000/G"));
        } catch (XccConfigException e) {
            e.printStackTrace();
        }
        String fileName = "/Users/ableasdale/Downloads/smallmbox.mbox.txt";

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

            //stream.forEach(doSomething());
            stream.forEach(s -> doSomething(s));

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("Total"+count);
    }

    private static void doSomething(String s) {
        // You'll see a "From " and an @xxx as part of GMails line separator; we can use this to split up the email and do "something" to it
        if(s.startsWith("From ") && s.contains("@xxx")) {
            LOG.info(s);
            if(sb.length() > 0){
                // At this point we have an email to parse
                //LOG.info(sb.toString());
                processEmail(sb.toString());

                sb.setLength(0);
            }
            count++;
        }
        sb.append(s).append(System.lineSeparator());
    }

    private static void processEmail(String s) {
        Session se = cs.newSession();

        try {
            se.submitRequest(se.newAdhocQuery("xdmp:document-insert('/"+ UUID.randomUUID()+".xml', binary{'"+s+"'})"));
                    //"xdmp:document-insert('/"+ UUID.randomUUID()+".xml', text {"+s+"})"));
        } catch (RequestException e) {
            e.printStackTrace();
        }
        //ContentSource cs = ContentSourceFactory
    }

}

