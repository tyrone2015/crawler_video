package porn91;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.logging.Logger;

/**
 * Created by yongqiang on 2015/2/1.
 */
public class Porn91 implements PageProcessor {
    public Logger logger = Logger.getLogger(this.getClass().getName());
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .addCookie("91porn.com", "language", "cn_CN")
            .addCookie("91porn.com", "watch_times", "0");
    String listUrl = "http://91porn\\.com/v\\.php\\w*";
    String videoUrl = "http://91porn\\.com/view_video\\.php\\w*";
    String downloadUrl = "http://91porn\\.com/getfile\\.php\\w*";

    @Override
    public void process(Page page) {
        System.out.println(page.getUrl().toString());
        if (page.getUrl().regex(listUrl).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@id='paging']//a/@href").all());
            page.addTargetRequests(page.getHtml().xpath("//div[@class='imagechannel']/a/@href").all());
        }
        if (page.getUrl().regex(videoUrl).match()) {
            String name = null;
            String VID = "123";
            String seccode = null;
            String max_vid = null;
            String other = "&mp4=1";
            Html currentHtml = page.getHtml();
            name = currentHtml.xpath("//div[@id='viewvideo-title']/text()").toString();
            VID = currentHtml.regex("\\w*file\',\'(\\d*)\'").toString();
            seccode = currentHtml.regex("\\w*seccode\\',\\'(\\w*)\'").toString();
            max_vid = currentHtml.regex("\\w*max_vid\',\'(\\d*)\'").toString();
            if (!VID.equals(null)) {
                String linnk = "http://91porn.com/getfile.php?VID=" + VID + "&seccode=" + seccode + "&max_vid=" + max_vid + other;
                page.addTargetRequest(new Request(linnk).putExtra("videoName", name));
            } else {
                logger.warning("ID" + page.getUrl());
            }
        }
        if (page.getUrl().regex(downloadUrl).match()) {
            String videoName = page.getRequest().getExtra("videoName").toString();
            String link = page.getHtml().xpath("//body/text()").toString().split("&")[0].split("=")[1];
            page.putField(videoName,
                    new Request(link).putExtra("videoName", videoName));
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

}