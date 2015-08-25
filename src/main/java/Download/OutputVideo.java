package Download;

import RunSpider.RunSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.utils.FilePersistentBase;

import java.util.Map;

/**
 * Created by only. on 2015/3/3.
 */
public class OutputVideo extends FilePersistentBase implements Pipeline {
    private String directory = null;

    /**
     * create a OutputUlr with default path"/data/webmagic/"
     */
    public OutputVideo(String path) {
        setPath(path);
    }

    public void process(ResultItems resultItems, Task task) {
        Logger logger = LoggerFactory.getLogger(this.getClass().getName());

        if (!resultItems.getRequest().toString().contains("videoName")) {
            logger.info("jump not videoName url");
            return;
        }
        directory = resultItems.getRequest().getExtra("videoName").toString().replaceAll(" ", "");
        Download download = null;
        for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
            if (entry.getValue() instanceof Iterable) {
                Iterable value = (Iterable) entry.getValue();
                for (Object o : value) {
                    download = new Download((Request) o, getPath() + directory, entry.getKey());
                    RunSpider.pool.execute(download);
                }
            } else {
                download = new Download((Request) entry.getValue(), getPath() + directory, entry.getKey());
                RunSpider.pool.execute(download);
            }
        }
    }
}

