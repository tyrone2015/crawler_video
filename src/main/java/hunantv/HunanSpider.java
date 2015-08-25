package hunantv;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

/**
 * Created by yongqiang on 2015/3/25.
 */
public class HunanSpider implements PageProcessor {
    private Site stie = Site.me().setRetryTimes(3).setTimeOut(5000).setSleepTime(100).addHeader("accept-encoding", "utf-8, deflate")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.101 Safari/537.36")
            .addHeader("Accept-Language", "en-US,en;q=0.8,zh-CN;q=0.6,zh;q=0.4,zh-TW;q=0.2");

    private final String Player = "http://v\\.api\\.hunantv\\.com\\w*";
    private final String Play = "http://www\\.hunantv\\.com\\w*";
    private final String cdn = "http://pcvcr\\.cdn\\.imgo\\.tv\\/ncrs\\w*";
    private final String pcvideows = "http://pcvideows\\.imgo\\.tv\\w*";

    @Override
    public void process(Page page) {

        if (page.getUrl().regex(Play).match()) {
            page.addTargetRequest("http://v.api.hunantv.com/player/video?video_id=" + page.getHtml().regex("vid:\\s(\\d+)"));
        }
        if (page.getUrl().regex(Player).match()) {
            Json json=page.getJson();
            int info = json.jsonPath(".data.stream").all().size();
            page.addTargetRequest(new Request(json.jsonPath(".data.stream[" + (info - 1) + "].url").toString()).putExtra("videoName",json.jsonPath(".data.info.title")));
        }
        if (page.getUrl().regex(cdn).match()){
            Json json=page.getJson();
            page.putField(page.getRequest().getExtra("videoName").toString()
                    ,new Request(json.jsonPath(".info").toString()+"?wshc_tag=1").putExtra("videoName",page.getRequest().getExtra("videoName")));
        }
    }

    @Override
    public Site getSite() {
        return stie;
    }

}
