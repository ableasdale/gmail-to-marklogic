import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.blueglacier.email.Attachment;
import tech.blueglacier.email.Email;
import tech.blueglacier.parser.CustomContentHandler;
import tech.blueglacier.util.MimeWordDecoder;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.codec.DecodeMonitor;
import org.apache.james.mime4j.message.DefaultBodyDescriptorBuilder;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.stream.BodyDescriptorBuilder;
import org.apache.james.mime4j.stream.MimeConfig;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class EmailMimeParserExamples {


        private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
        static int count = 0;
        static StringBuilder sb = new StringBuilder();

        public static void main (String[]args){

            String fileName = "/Users/ableasdale/Downloads/smallmbox.mbox.txt";

            //read file into stream, try-with-resources
            try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

                //stream.forEach(doSomething());
                stream.forEach(s -> doSomething(s));

            } catch (IOException e) {
                e.printStackTrace();
            }

            LOG.info("Total" + count);
        }

        private static void doSomething (String s){
            if (s.startsWith("From")) {
                if (sb.length() > 0) {
                    // At this point we have an email to parse
                    //LOG.info(sb.toString());
                    parseEmail(sb.toString());

                    sb.setLength(0);
                }
                count++;
            }
            sb.append(s).append(System.lineSeparator());
        }

        private static void parseEmail (String s){

            ContentHandler contentHandler = new CustomContentHandler();

            MimeConfig mime4jParserConfig = MimeConfig.DEFAULT;
            BodyDescriptorBuilder bodyDescriptorBuilder = new DefaultBodyDescriptorBuilder();
            MimeStreamParser mime4jParser = new MimeStreamParser(mime4jParserConfig, DecodeMonitor.SILENT, bodyDescriptorBuilder);
            mime4jParser.setContentDecoding(true);
            mime4jParser.setContentHandler(contentHandler);

            //String fileName = "/Users/ableasdale/Downloads/smallmbox.mbox.txt";
            InputStream mailIn = null;
            try {
                mailIn = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
                mime4jParser.parse(mailIn);

                Email email = ((CustomContentHandler) contentHandler).getEmail();

                List<Attachment> attachments = email.getAttachments();

                Attachment calendar = email.getCalendarBody();
                Attachment htmlBody = email.getHTMLEmailBody();
                Attachment plainText = email.getPlainTextEmailBody();

                String to = email.getToEmailHeaderValue();
                LOG.info("To: "+to);
                String cc = email.getCCEmailHeaderValue();
                String from = email.getFromEmailHeaderValue();
                LOG.info("From: "+from);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MimeException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }