package PornHub;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * Created by tailong on 2015/6/5.
 */
public class PornHub implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setTimeOut(5000).setSleepTime(100).addHeader("accept-encoding", "utf-8, deflate")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
            .addHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2");

    @Override
    public void process(Page page) {

        if (page.getUrl().toString().contains("embed")) {
            Html embadPage =page.getHtml();
            String videoName=embadPage.xpath("//title/text()").toString();
            String downlaodUrl=embadPage.regex("src\\t\\t\\:\\s\\'(.*)\\'\\,[\\s\\S]*?poster").toString();
            page.putField(videoName,new Request(downlaodUrl).putExtra("videoName",videoName));
        }else {
            page.addTargetRequest(page.getUrl().toString().replaceAll("view_video\\.php\\?viewkey=", "embed/"));
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

}
