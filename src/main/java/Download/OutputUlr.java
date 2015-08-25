package Download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.PrivateKey;
import java.util.Map;

/**
 * Created by only. on 2015/3/2.
 */
public class OutputUlr extends FilePersistentBase implements Pipeline {
    /**
     * create a OutputUlr with default path"/data/webmagic/"
     */
    public OutputUlr() {
        setPath("/data/webmagic/");
    }

    public OutputUlr(String path) {
        setPath(path);
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        Logger logger = LoggerFactory.getLogger(this.getClass().getName());
        try {

            if (!resultItems.getRequest().toString().contains("videoName")) {
                logger.info("jump not videoName url");
                return;
            }

            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(
                    getFile(getPath() + resultItems.getRequest().getExtra("videoName").toString() + ".txt")
                    , true), "UTF-8"));

            for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
                if (entry.getValue() instanceof Iterable) {
                    Iterable value = (Iterable) entry.getValue();
                    printWriter.println(entry.getKey());
                    for (Object o : value) {
                        printWriter.println(((Request) o).getExtra("subsection") + ((Request) o).getUrl());
                    }
                } else {
                    Request request = (Request) entry.getValue();
                    printWriter.println(entry.getKey() + ":\t" + request.getUrl());
                }
            }
            printWriter.close();
        } catch (IOException e) {

        }
    }
}
