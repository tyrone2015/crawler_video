package RunSpider;

import Download.OutputUlr;
import PornHub.PornHub;
import hunantv.HunanSpider;
import porn91.Porn91;
import sohu.SohuSpider;
import Download.OutputVideo;
import tools.UserInfo;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.monitor.SpiderMonitor;
import us.codecraft.webmagic.processor.PageProcessor;

import javax.management.JMException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by tailong on 2015/6/11.
 */
public class RunSpider {

    private static Spider video = null;
    public static ExecutorService pool = Executors.newFixedThreadPool(5);//设置下载线程数

    public static void main(String[] args) throws JMException {
        UserInfo info = new UserInfo();
        if (args.length == 0) {
            video = Spider.create(info.getPlatform(info.getUserInfo().get(0)))
                    .addUrl(info.Urls().toArray(new String[info.Urls().size()]))
                    .addPipeline(new OutputVideo(info.getFilePath()));
            SpiderMonitor.instance().register(video);
            video.thread(2).run();
        } else {
            video = Spider.create(info.getPlatform(args[0]))
                    .addUrl(args)
                    .addPipeline(new OutputUlr("E://test/url/"))
                    .addPipeline(new OutputVideo(info.getFilePath()));
            SpiderMonitor.instance().register(video);
            video.run();
        }
    }

}
