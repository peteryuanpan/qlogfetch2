package com.peter.execute.downlog;

import com.peter.execute.ExecuteHttp;
import com.peter.model.DownLog;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public abstract class ExecuteDownLogAbstract implements ExecuteDownLogInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteDownLogAbstract.class);

    protected void beforeRequestSend(Request request) {
        // for subclass
    }

    @Override
    public ExecuteDownLog.Response execute(DownLog downLog, String[] urls) throws Exception {
        ExecuteDownLog.Response resp = new ExecuteDownLog.Response();

        final long begin = System.currentTimeMillis();
        final AtomicLong totalSize = new AtomicLong(0);
        final AtomicInteger successNumber = new AtomicInteger(0);
        final int worker = downLog.getWorker();
        final String dest = downLog.getDest();
        final CountDownLatch countDownLatch = new CountDownLatch(urls.length);
        final ExecuteHttp http = ExecuteHttp.getInstance();

        logger.debug("urls.length: " + urls.length);
        logger.debug("worker: " + worker);
        logger.debug("dest: " + dest);

        final File destF = new File(dest);
        if (!destF.exists())
            destF.mkdirs();

        final ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            worker, worker,
            0, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>()
        );

        for (int w = 0; w < urls.length; w ++) {
            final int i = w;
            threadPool.submit(new Thread(() -> {
                String filePath = destF.getPath() + "/" + urls[i].substring(urls[i].lastIndexOf('/') + 1, urls[i].indexOf('?'));
                logger.debug("filePath: " + filePath);
                if (!downLog.isOverwrite() && new File(filePath).exists()) {
                    logger.info("Skip to download url " + urls[i] + " because file " + filePath + " exists");
                    successNumber.addAndGet(1);
                    return;
                }
                Response response = null;
                byte[] bytes = null;
                int j = 0;
                try {
                    while (j <= downLog.getRetry()) {
                        Request request = http.getRequest(urls[i]);
                        beforeRequestSend(request);
                        logger.debug("retry times " + j + ", " + request.toString());
                        try {
                            response = http.get(request);
                            logger.debug("retry times " + j + ", " + response.toString());
                            if (response.code() == 200) {
                                bytes = response.body().bytes(); // important
                                break;
                            }
                        } catch (Exception e) {
                            logger.info("retry times " + j + ", Error: " + e.getClass().getName() + ", ErrorMessage: " + e.getMessage() + ", failed to download url " + urls[i]);
                        }
                        j ++;
                    }
                    if (j <= downLog.getRetry()) { // code = 200
                        OutputStream outputStream = new FileOutputStream(filePath);
                        outputStream.write(bytes);
                        outputStream.flush();
                        outputStream.close();
                        logger.info("Success to download url " + urls[i] + " size " + ExecuteDownLog.getSize(bytes.length));
                        totalSize.addAndGet(bytes.length);
                        successNumber.addAndGet(1);
                    } else
                        logger.info("Failed to download url " +  urls[i] + " after retrying " + downLog.getRetry());
                } catch (Exception e) {
                    logger.error(e.getClass().getName() + " " + e.getMessage());
                } finally {
                    if (response != null)
                        response.close();
                }
                countDownLatch.countDown();
            }));
        }

        threadPool.shutdown();
        countDownLatch.await();

        resp.totalSize = totalSize.get();
        resp.totalTimeCost = (System.currentTimeMillis() - begin) / 1000.0;
        resp.successNumber = successNumber.get();
        resp.failedNumber = urls.length - successNumber.get();
        return resp;
    }
}
