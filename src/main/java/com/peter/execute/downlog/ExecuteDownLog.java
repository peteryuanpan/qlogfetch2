package com.peter.execute.downlog;

import com.peter.execute.ExecuteInfo;
import com.peter.execute.listlog.ExecuteListLog;
import com.peter.execute.listlog.ExecuteListLogInterface;
import com.peter.model.Account;
import com.peter.model.DownLog;
import com.peter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class ExecuteDownLog {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteDownLog.class);

    public static void downloadLogs(DownLog downLog) throws Exception {
        Account account = ExecuteInfo.readAccountJsonFile();
        String[] urls;
        logger.info("Start to list log urls of domain");
        String className = ExecuteListLog.class.getName() + account.getSource().getUpperCaseName();
        Class<?> clazz = Class.forName(className);
        ExecuteListLogInterface instance = (ExecuteListLogInterface) clazz.newInstance();
        ExecuteListLog.Response response = instance.execute(account, downLog.getListLog());
        assert response != null && response.getUrls() != null;
        if (response.getCode() != 200) {
            logger.info("Failed to list log urls of domain");
            logger.info("code: " + response.getCode());
            logger.info("message: " + response.getMessage());
            logger.info("You can add '-d' to print debug log");
            return;
        }
        urls = response.getUrls();
        logger.info("Finished listing log urls of domain");
        downloadLogs(downLog, urls);
    }

    public static void downloadLogs(DownLog downLog, String filePath) throws Exception {
        logger.info("Start to read urls from " + filePath);
        BufferedReader br = new BufferedReader(new FileReader(filePath));
        List<String> lines = new ArrayList();
        String line;
        while ((line = br.readLine()) != null) {
            if (!StringUtil.isEmpty(line))
                lines.add(line);
        }
        logger.info("Finished reading urls from " + filePath);
        downloadLogs(downLog, lines.toArray(new String[0]));
    }

    public static void downloadLogs(DownLog downLog, String[] urls) throws Exception {
        Account account = ExecuteInfo.readAccountJsonFile();
        logger.info("Start to download urls");
        String className = ExecuteDownLog.class.getName() + account.getSource().getUpperCaseName();
        Class<?> clazz = Class.forName(className);
        ExecuteDownLogInterface instance = (ExecuteDownLogInterface) clazz.newInstance();
        Response response = instance.execute(downLog, urls);
        logger.info("Finished downloading urls");
        logger.info("TotalSize: " + getSize(response.totalSize));
        logger.info("TotalTimeCost: " + response.totalTimeCost + " second");
        logger.info("AverageSpeed: " + getSize(response.totalSize / response.totalTimeCost) + "/s");
        logger.info("SuccessNumber: " + response.successNumber);
        logger.info("FailedNumber: " + response.failedNumber);
    }

    private static final String[] unit = new String[]{"B", "KB", "MB", "GB"};

    static String getSize(double size0) {
        double size = size0;
        for (int i = 0; i < unit.length - 1; i ++) {
            if (size < 1024)
                return String.format("%.2f %s", size, unit[i]);
            size = size / 1024;
        }
        return String.format("%.2f %s", size, unit[unit.length-1]);
    }

    public static class Response {
        long totalSize;
        double totalTimeCost;
        int successNumber;
        int failedNumber;
    }
}
