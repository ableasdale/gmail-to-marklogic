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
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Uses Apache James to parse email content - seems a little bit unreliable
 */

public class ScanMBoxForMessages {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static int count = 0;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) {

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
        if(s.startsWith("From") ) {
            if(sb.length() > 0){
                // At this point we have an email to parse
                //LOG.info(sb.toString());
                parseEmail(sb.toString());

                sb.setLength(0);
            }
            count++;
        }
        sb.append(s).append(System.lineSeparator());
    }

    private static void parseEmail(String s) {
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
    }
    }

