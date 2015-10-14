package tools;

import PornHub.PornHub;
import hunantv.HunanSpider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import porn91.Porn91;
import sohu.SohuSpider;
import us.codecraft.webmagic.processor.PageProcessor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by tailong on 2015/6/11.
 */
public class UserInfo {
    private static String dirPath = null;
    private static PageProcessor platform = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 根据URL 实例不同的类
     *
     * @param data
     * @return
     */
    public PageProcessor getPlatform(String data) {
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

    /**
     * 获取用户输入的URL
     *
     * @return
     */
    public String[] getUserInputUrls() {
        String[] result = null;
        System.out.println("please input link, if your input String contains more link, Please add ','to the link between");
        Scanner sc = new Scanner(System.in);
        String url = sc.nextLine();
        if (url.isEmpty()) {
            System.out.println("url is empty, spider exit.");
            System.exit(-1);
        }
        if (url.contains(",")) {
            result = url.split(",");
        } else {
            result = new String[1];
            result[0] = url;
        }
        return result;
    }

    /**
     * 获取系统最大剩余空间的分区
     *
     * @return
     */
    public String getDriverMaxSpace() {
        String os = System.getProperty("os.name");
        if (os.contains("Windows")) {
            int max = 0;
            File[] roots = File.listRoots();
            for (int i = 0; i < roots.length; i++) {
                long spaceSize = roots[0].getFreeSpace();
                if (roots[i].getFreeSpace() > spaceSize) {
                    max = i;
                    continue;
                }
                dirPath = roots[max].getPath() + "video";
            }
        } else {
            //linux df -m |awk 'NR>1{print $1,$4}'
            BufferedReader br = null;
            String[] shell = new String[]{"sh", "-c", "df -m |awk 'NR>1{print $6\",\"$4}'"};
            Map<String, String> result = new HashMap<String, String>();

            try {
                Process process = Runtime.getRuntime().exec(shell);
                process.waitFor();
                br = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    result.put(line.split(",")[1].replaceAll(",",""), line.split(",")[0]);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long max = 0;
            for (Map.Entry<String, String> entry : result.entrySet()) {
                if (Integer.valueOf(entry.getKey()) > max) {
                    max = Integer.valueOf(entry.getKey());
                }
            }
            dirPath = result.get(String.valueOf(max))+ "video_linux";
        }
        new FileInfo().checkSubsection(dirPath);
        logger.info("use dir path is : " + dirPath);
        return dirPath;
    }


}

