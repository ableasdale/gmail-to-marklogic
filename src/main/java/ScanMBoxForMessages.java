import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ScanMBoxForMessages {

    private static Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    static int count = 0;
    static StringBuilder sb = new StringBuilder();

    public static void main(String[] args) {

        String fileName = "E:\\smallmbox.mbox.txt";

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
                LOG.info(sb.toString());

                sb.setLength(0);
            }
            count++;
        }
        sb.append(s);
    }
}
