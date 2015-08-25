package sohu;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by only. on 2015/1/12.
 */
public class SohuSpider implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setTimeOut(5000).setSleepTime(100).addCookie(".tv.sohu.com", "isJump", "0");
    private String MOVIE_LIST = "http://so\\.tv\\.sohu\\.com\\/list\\w+";
    private String MOVIE_HOME = "http://tv\\.sohu\\.com\\/item\\/\\w+";
    private String MOVIE_URL = "http://tv\\.sohu\\.com\\/\\w+\\/\\w+\\.shtml";
    private String MOVIE_URL_ALL = "http://pl.hd.sohu.com/videolist";
    private String DOWNLOAD_URL = "http://hot\\.vrs\\.sohu\\.com\\/\\w+";


    @Override
    public void process(Page page) {
        if (page.getUrl().regex(MOVIE_LIST).match()) {
            page.addTargetRequests(page.getHtml().xpath("//div[@class='ssPages area']/a/@href").all());
            page.addTargetRequests(page.getHtml().xpath("//strong/a/@href").all());
        }

        if (page.getUrl().regex(MOVIE_HOME).match()) {
            String link = page.getHtml().xpath("//a[@class='btn-playFea']/@href").toString();
            //获取电影名称加入请求中
            page.addTargetRequest(new Request(link).putExtra("videoName", page.getHtml().xpath("//span[@class='vname']/text()").toString()));
        }

        if (page.getUrl().regex(MOVIE_URL).match()) {
            if (page.getRequest().getExtra("videoName") == null) {
                //获取电影名称加入请求中
                page.getRequest().putExtra("videoName", page.getHtml().xpath("//meta[@name='album']/@content"));
            }
            String link = "http://hot.vrs.sohu.com/vrs_flash.action?vid=" + page.getHtml().regex("vid=\"(\\d*)").toString() + "&bw=2048";//&af=1";
            //获取电影名称加入请求中
            page.addTargetRequest(new Request(link).putExtra("videoName", page.getRequest().getExtra("videoName")).putExtra("vid", page.getHtml().regex("vid=\"(\\d*)").toString()));

            String plLink = "http://pl.hd.sohu.com/videolist?playlistid=" + page.getHtml().regex("playlistId=\"(\\d*)");
            //获取电影名称加入请求中
            page.addTargetRequest(new Request(plLink).putExtra("videoName", page.getRequest().getExtra("videoName")));

        }

        if (page.getUrl().regex(MOVIE_URL_ALL).match()) {

            for (String link : page.getJson().jsonPath(".videos.pageUrl").all()) {
                page.addTargetRequest(new Request(link).putExtra("videoName", page.getRequest().getExtra("videoName")));
            }

        }

        if (page.getUrl().regex(DOWNLOAD_URL).match()) {
            String vid = getHighId(page.getJson());
            if (page.getRequest().getExtra("vid").equals(vid)) {
                List videoUrl = page.getJson().jsonPath(".data.su").all();
                String videoName = page.getJson().jsonPath(".data.tvName").toString();
                List<Request> dowmlaods = new ArrayList<Request>();
                for (int i = 0; i < videoUrl.size(); i++) {
                    String link = "http://202.98.156.30/sohu.vodnew.lxdns.com/sohu/s26h23eab6" + videoUrl.get(i);
                    dowmlaods.add(new Request(link).putExtra("videoName", page.getRequest().getExtra("videoName")).putExtra("subsection", "第" + i + "段:"));
                }
                page.putField(videoName, dowmlaods);
            } else {
                page.addTargetRequest(new Request("http://hot.vrs.sohu.com/vrs_flash.action?vid=" + vid + "&bw=2048").putExtra("vid", vid)
                        .putExtra("videoName", page.getRequest().getExtra("videoName")));
            }
        }


    }

    @Override
    public Site getSite() {
        return site;
    }

    private String getHighId(Json json) {
        String result = null;
        if (!json.jsonPath(".oriVid").toString().equals("0")) {
            result = json.jsonPath(".oriVid").toString();
        } else if (!json.jsonPath(".superVid").toString().equals("0")) {
            result = json.jsonPath(".superVid").toString();
        } else if (!json.jsonPath(".highVid").toString().equals("0")) {
            result = json.jsonPath(".highVid").toString();
        } else if (!json.jsonPath(".norVid").toString().equals("0")) {
            result = json.jsonPath(".norVid").toString();
        }
        return result;
    }
}
