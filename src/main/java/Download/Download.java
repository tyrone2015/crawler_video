package Download;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.FileInfo;
import us.codecraft.webmagic.Request;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yongqiang on 2015/3/6.
 */
public class Download extends Thread {
    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpGet httpget;
    private String downloadPath = null;
    private String videoName = null;
    private FileInfo fileInfo = null;
    private String directory = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    public Download(Request request, String directory, String videoName) {
        this.fileInfo = new FileInfo();
        this.httpClient = HttpClients.createDefault();
        this.context = HttpClientContext.create();
        this.httpget = new HttpGet(request.getUrl().toString());
        this.videoName = videoName;
        this.directory = directory;
        setHeader(request);
        setFileName(request);
    }

    private void setHeader(Request request) {
        this.httpget.addHeader("Referer", request.getUrl().toString());
    }

    private synchronized double getFileSize(CloseableHttpResponse response) {
        double fileSize = Long.valueOf(response.getHeaders("Content-Length")[0].getValue());
        return fileSize;
    }

    private synchronized void setFileName(Request request) {
        if (request.toString().contains("subsection")) {
            this.videoName = videoName + request.getExtra("subsection");
        } else {
            this.videoName = videoName + "Full";
        }

    }


    public void run() {
        try {
            CloseableHttpResponse response = httpClient.execute(httpget, context);
            try {
                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {

                        fileInfo.checkSubsection(directory + "/");
                        downloadPath = directory + "/" + this.videoName.replaceAll(":", "") + ".mp4";

                        if (fileInfo.checkFilesExists(downloadPath, getFileSize(response))) {

                            if (entity.getContentLength() != -1) {

                                fileInfo.loading(entity.getContent(), new FileOutputStream(new File(downloadPath)), getFileSize(response), videoName);

                            } else {
                                logger.info("get connect length is -1");
                            }
                        } else {
                            logger.info("jump is" + videoName);
                        }
                    } else {
                        logger.info("not response deta");
                    }
                } else {
                    logger.info("page status code  is:" + response.getStatusLine().getStatusCode());
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException ex) {
            // Handle protocol errors
            logger.info(ex.toString());
        } catch (IOException ex) {
            // Handle I/O errors
            logger.info(ex.toString());
        }
    }
}
