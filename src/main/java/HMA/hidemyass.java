package HMA;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tailong on 2015/6/15.
 */
public class hidemyass implements PageProcessor {
    Logger logger= LoggerFactory.getLogger(this.getClass().getName());
    private Site site = Site.me().setRetryTimes(3).setTimeOut(5000).setSleepTime(100).addHeader("accept-encoding", "utf-8, deflate")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
            .addHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2");

    private List<String> result=new ArrayList<String>();
    @Override
    public void process(Page page) {
        logger.warn(page.getHtml().xpath("//tbody/tr[1]/td[2]/span/style/text()").toString()+"bbbbb");
    }

    @Override
    public Site getSite() {
        return site;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }
}
