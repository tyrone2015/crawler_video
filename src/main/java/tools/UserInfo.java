package tools;

import PornHub.PornHub;
import hunantv.HunanSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import porn91.Porn91;
import sohu.SohuSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tailong on 2015/6/11.
 */
public class UserInfo {
    private static Reader in;
    private static BufferedReader br = null;
    private static List<String> info = null;
    private static String path = null;
    private static String dirPath = null;
    private static List<String> result = new ArrayList<String>();
    private static PageProcessor platform = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public  PageProcessor getPlatform(String data) {
        if (data.contains("sohu")) {
            platform = new SohuSpider();
        } else if (data.contains("hunantv")) {
            platform = new HunanSpider();
        } else if (data.contains("pornhub")) {
            platform = new PornHub();
        } else if (data.contains("91porn")) {
            platform = new Porn91();
        } else {
            System.exit(-1);
        }
        return platform;
    }

    public  List<String> getUserInfo() {
        try {
            path = getDriverMaxSpace() + "/info";
            info = new ArrayList<String>();
            in = new FileReader(path);
            br = new BufferedReader(in);
            String line = "";
            while ((line = br.readLine()) != null) {
                info.add(line);
            }
            br.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    public  String getDriverMaxSpace() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            int max = 0;
            File[] roots = File.listRoots();
            for (int i = 0; i < roots.length; i++) {
                long spaceSize = roots[0].getFreeSpace();
                if (roots[i].getFreeSpace() > spaceSize) {
                    spaceSize = roots[i].getFreeSpace();
                    max = i;
                    continue;
                }
                dirPath = roots[max].getPath() + "video";
            }
        } else {
            //linux

        }
        new FileInfo().checkSubsection(dirPath);
        logger.info("use dir path is : " + dirPath);
        return dirPath;
    }

    public  List<String> Urls() {
        for (String s : getUserInfo()) {
            if (s.contains("http")) {
                result.add(s);
            }
        }
        return result;
    }


}

