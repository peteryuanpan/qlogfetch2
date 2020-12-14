package com.peter.command;

import com.peter.execute.ExecuteDebug;
import com.peter.execute.downlog.ExecuteDownLog;
import com.peter.model.DownLog;
import com.peter.model.ListLog;
import com.peter.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CommandDownList implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandDownList.class);

    @Override
    public String commandName() {
        return "downlist";
    }

    @Override
    public int priority() {
        return 5;
    }

    private final DownLog downLog = new DownLog();
    private String sourceFilePath;

    private ListLog getListLog() {
        ListLog listLog = downLog.getListLog();
        if (listLog == null)
            downLog.setListLog(listLog = new ListLog());
        return listLog;
    }

    @Override
    public void execute(String[] args) throws Exception {
        if (!leggal(args)) {
            System.out.print(getHelp());
            return;
        }
        ExecuteDownLog.downloadLogs(downLog, sourceFilePath);
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        int i = 1;
        int status = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "-src":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -src");
                        return false;
                    }
                    sourceFilePath = args[i];
                    status |= 0x000001;
                    break;
                case "-dest":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -dest");
                        return false;
                    }
                    downLog.setDest(args[i]);
                    status |= 0x000010;
                    break;
                case "-overwrite":
                    downLog.setOverwrite(true);
                    status |= 0x000100;
                    break;
                case "-worker":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -worker");
                        return false;
                    }
                    int worker;
                    try {
                        worker = Integer.parseInt(args[i]);
                    } catch (Exception e) {
                        logger.info("Worker '" + args[i] + "' is not an integer");
                        return false;
                    }
                    if (worker < 0 || worker > 300) {
                        logger.info("Worker '" + worker + "' not in range [1, 300]");
                        return false;
                    }
                    downLog.setWorker(worker);
                    status |= 0x001000;
                    break;
                case "-retry":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -retry");
                        return false;
                    }
                    int retry;
                    try {
                        retry = Integer.parseInt(args[i]);
                    } catch (Exception e) {
                        logger.info("Retry '" + args[i] + "' is not an integer");
                        return false;
                    }
                    if (retry < 0 || retry > 10) {
                        logger.info("Retry '" + retry + "' not in range [1, 10]");
                        return false;
                    }
                    downLog.setRetry(retry);
                    status |= 0x010000;
                    break;
                case "-d":
                    ExecuteDebug.setAllLoggerLevelDebug();
                    status |= 0x100000;
                    break;
                default:
                    logger.info("Unknown parameter " + args[i]);
                    return false;
            }

            // important
            i ++;
        }
        if ((status & 0x000001) == 0)
            logger.info("Missing parameter -src");
        if ((status & 0x000010) == 0)
            logger.info("Missing parameter -dest");
        if ((status & 0x000100) == 0)
            downLog.setOverwrite(false);
        if ((status & 0x001000) == 0)
            downLog.setWorker(Constants.DefaultConcurrencyLevel);
        if ((status & 0x010000) == 0)
            downLog.setRetry(Constants.DefaultRetryTimes);
        return (status & 0x000011) == 0x000011;
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
