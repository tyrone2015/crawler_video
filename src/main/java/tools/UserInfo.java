package tools;

import PornHub.PornHub;
import hunantv.HunanSpider;
import porn91.Porn91;
import sohu.SohuSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.*;
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
    private static List<String> result = new ArrayList<String>();
    private static PageProcessor platform = null;

    public static PageProcessor getPlatform(String data) {
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

    public static List<String> getUserInfo() {
        try {
            path = "E:/test/info";
//            path = getFilePath() + "info.txt";
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

    public static String getFilePath() {
        String path = System.getProperty("java.class.path");
        int firstIndex = path.lastIndexOf(System.getProperty("path.separator")) + 1;
        int lastIndex = path.lastIndexOf(File.separator) + 1;
        path = path.substring(firstIndex, lastIndex);
        return "E:/test/";
//        return path;
    }

    public static List<String> Urls() {
        for (String s : getUserInfo()) {
            if (s.contains("http")) {
                result.add(s);
            }
        }
        return result;
    }


}

