package tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * Created by tailong on 2015/6/12.
 */
public class FileInfo {
    private File filePath = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * 检查下载的文件目录是否存在
     *
     * @param directory
     */
    public synchronized void checkSubsection(String directory) {
        filePath = new File(directory);
        if (filePath.exists() == false) {
            filePath.mkdirs();
        }
    }

    /**
     * 检查当前需要下载的文件是否存在
     *
     * @param path
     * @param fileSize
     * @return
     */
    public synchronized boolean checkFilesExists(String path, double fileSize) {
        boolean result = true;
        try {
            if (new File(path).exists()) {
                InputStream in = new FileInputStream(new File(path));
                if (in.available() == fileSize) {
                    result = false;
                }
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 视频写入
     *
     * @param in
     * @param out
     * @param fileSize
     * @param videoName
     */
    public synchronized void loading(InputStream in, OutputStream out, double fileSize, String videoName) {
        byte[] buffer = new byte[10240];
        int readLength = 0;
        double downloadSize = 0;
        try {
            while ((readLength = in.read(buffer)) > 0) {
                downloadSize += readLength;
                out.write(buffer, 0, readLength);
                out.flush();
//                System.out.print('\r');
                logger.info(Thread.currentThread().getName() + "download thread");
                logger.info(videoName + "\t" + String.format("%.2f", downloadSize / fileSize * 100) + "%");//downloadSize / fileSize * 100
            }
            in.close();
            out.close();
        } catch (IOException e) {
            logger.warn(Thread.currentThread().getName() + "Thread is warn");
            e.printStackTrace();
        }

    }

}
