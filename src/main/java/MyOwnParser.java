import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.ContentSourceFactory;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.exceptions.RequestException;
import com.marklogic.xcc.exceptions.XccConfigException;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.stream.Field;
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
        String fileName = "C:\\Users\\alexb\\Downloads\\All mail Including Spam and Trash-021.mbox";
        // "e:\\smallmbox.mbox.txt";

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName), StandardCharsets.UTF_8)) {

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
        } else {
            // For now - we're discarding the first line just in case it makes the standard java parsers any better
            sb.append(s).append(System.lineSeparator());
        }
    }

    private static void processEmail(String s) {
        /*
        Session se = cs.newSession();

        try {
            se.submitRequest(se.newAdhocQuery("xdmp:document-insert('/"+ UUID.randomUUID()+".xml', element data {'<![CDATA["+s+"]]>'})"));
                    //"xdmp:document-insert('/"+ UUID.randomUUID()+".xml', text {"+s+"})"));
        } catch (RequestException e) {
            e.printStackTrace();
        } */
        //ContentSource cs = ContentSourceFactory
        //LOG.info("Parsing "+s);
        MessageBuilder builder; // = new DefaultMessageBuilder().;
        DefaultMessageBuilder dmb = new DefaultMessageBuilder();
        MimeConfig mc = new MimeConfig.Builder().setMaxLineLen(2147483647).setMaxHeaderLen(2147483647).setMaxLineLen(2147483647).build();
        dmb.setMimeEntityConfig(mc);
        builder = dmb;

        Message message = null;
        try {
            message = builder.parseMessage( new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8)));
            //message.getSubject()
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MimeException e) {
            e.printStackTrace();
        }
        //   s.getBytes());
        LOG.info(String.format("\nMessage %s \n" +
                        "Sent by:\t%s\n" +
                        "To:\t%s\n",
                message.getSubject(),
                message.getSender(),
                message.getTo()));

        StringBuilder sb = new StringBuilder();
        sb.append("<GMailMessage><Header>");
        for (Field f: message.getHeader().getFields()){
            sb.append("<").append(f.getName()).append(">");
            sb.append(StringEscapeUtils.escapeXml(f.getBody()));
            sb.append("</").append(f.getName()).append(">");
        }
        sb.append("</Header><Body>").append(StringEscapeUtils.escapeXml(message.getBody().toString())).append("</Body></GMailMessage>");


        LOG.info(sb.toString());
        Session se = cs.newSession();

        try {
            // TODO - store entire doc as binary se.submitRequest(se.newAdhocQuery("xdmp:document-insert('/"+ UUID.randomUUID()+".xml', element data {'<![CDATA["+s+"]]>'})"));
            se.submitRequest(se.newAdhocQuery("xdmp:document-insert('/"+ UUID.randomUUID()+".xml', "+sb.toString()+")"));
            //"xdmp:document-insert('/"+ UUID.randomUUID()+".xml', text {"+s+"})"));
        } catch (RequestException e) {
            e.printStackTrace();
        }

    }



}

