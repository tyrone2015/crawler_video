package RunSpider;

import Download.OutputUlr;
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

    private static Spider video = null;
    public static ExecutorService myPool = Executors.newFixedThreadPool(5);//设置下载线程数

    public static void main(String[] args) throws JMException {
        UserInfo info = new UserInfo();
        if (args.length == 0) {
            video = Spider.create(info.getPlatform(info.getUserInfo().get(0)))
                    .addUrl(info.Urls().toArray(new String[info.Urls().size()]))
                    .addPipeline(new OutputVideo(info.getDriverMaxSpace()));
            SpiderMonitor.instance().register(video);
            video.thread(5).run();
        } else {
            video = Spider.create(info.getPlatform(args[0]))
                    .addUrl(args)
                    .addPipeline(new OutputUlr(info.getDriverMaxSpace()))
                    .addPipeline(new OutputVideo(info.getDriverMaxSpace()));
            SpiderMonitor.instance().register(video);
            video.run();
        }
    }

}
