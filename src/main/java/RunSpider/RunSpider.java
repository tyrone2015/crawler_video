package RunSpider;

import Download.OutputVideo;
import tools.UserInfo;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;

import javax.management.JMException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tailong on 2015/6/11.
 */
public class RunSpider {

    public static Spider video = null;
    public static ExecutorService myPool = Executors.newFixedThreadPool(20);//设置下载线程数

    public static void main(String[] args) throws JMException {
        UserInfo info = new UserInfo();
        String[] url = info.getUserInputUrls();
//        String url="http://tv.sohu.com/item/MTE5MjIzMw==.html";
        video = Spider.create(info.getPlatform(url[0]))
                .addUrl(url)
                .addPipeline(new OutputVideo(info.getDriverMaxSpace()));
        SpiderMonitor.instance().register(video);
        video.run();
        myPool.shutdown();
        System.out.println("thread is shutdown ...........");
    }

}
