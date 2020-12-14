package com.peter.command;

import com.peter.execute.ExecuteDebug;
import com.peter.execute.downlog.ExecuteDownLog;
import com.peter.model.DownLog;
import com.peter.model.ListLog;
import com.peter.util.Constants;
import com.peter.util.DateUtil;
import com.peter.util.DomainUtil;
import com.peter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandDownLog implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandDownLog.class);

    @Override
    public String commandName() {
        return "downlog";
    }

    @Override
    public int priority() {
        return 4;
    }

    private final DownLog downLog = new DownLog();

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
        ExecuteDownLog.downloadLogs(downLog);
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        int i = 1;
        int status = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "-date":
                    i ++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -date");
                        return false;
                    }
                    String[] ds = args[i].split(":", 3);
                    if (ds.length > 2) {
                        logger.info("Date " + args[i] + " has wrong format");
                        return false;
                    }
                    if (ds.length == 1 || ds.length == 2) {
                        String ds1 = ds[0];
                        Date date1 = DateUtil.parseDate(ds1, true);
                        if (date1 == null) {
                            logger.info("Date '" + ds1 + "' has wrong format");
                            return false;
                        }
                        String ds2 = ds.length == 1 ? ds[0] : ds[1];
                        Date date2 = DateUtil.parseDate(ds2, false);
                        if (date2 == null) {
                            logger.info("Date '" + ds2 + "' has wrong format");
                            return false;
                        }
                        if (date1.after(date2)) {
                            logger.info("Date '" + ds1 + "' is after than Date '" + ds2 + "'");
                            return false;
                        }
                        getListLog().setFromDate(date1);
                        getListLog().setToDate(date2);
                    }
                    status |= 0x0000001;
                    break;
                case "-domains":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -domains");
                        return false;
                    }
                    String[] domains = args[i].split(";");
                    List<String> list = new ArrayList<>();
                    StringBuilder sb = new StringBuilder();
                    for (String domain : domains) {
                        if (StringUtil.isEmpty(domain))
                            continue;
                        if (!DomainUtil.isValid(domain)) {
                            logger.info("Domain '" + domain + "' has wrong format");
                            return false;
                        }
                        list.add(domain);
                        sb.append(sb.toString().isEmpty() ? "" : ";").append(domain);
                    }
                    getListLog().setDomains(list.toArray(new String[0]));
                    getListLog().setDomainStr(sb.toString());
                    status |= 0x0000010;
                    break;
                case "-dest":
                    i++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -dest");
                        return false;
                    }
                    downLog.setDest(args[i]);
                    status |= 0x0000100;
                    break;
                case "-overwrite":
                    downLog.setOverwrite(true);
                    status |= 0x0001000;
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
                    status |= 0x0010000;
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
                    status |= 0x0100000;
                    break;
                case "-d":
                    ExecuteDebug.setAllLoggerLevelDebug();
                    status |= 0x1000000;
                    break;
                default:
                    logger.info("Unknown parameter " + args[i]);
                    return false;
            }

            // important
            i ++;
        }
        if ((status & 0x0000001) == 0)
            logger.info("Missing parameter -date");
        if ((status & 0x0000010) == 0)
            logger.info("Missing parameter -domains");
        if ((status & 0x0000100) == 0)
            logger.info("Missing parameter -dest");
        if ((status & 0x0001000) == 0)
            downLog.setOverwrite(false);
        if ((status & 0x0010000) == 0)
            downLog.setWorker(Constants.DefaultConcurrencyLevel);
        if ((status & 0x0100000) == 0)
            downLog.setRetry(Constants.DefaultRetryTimes);
        return (status & 0x0000111) == 0x0000111;
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
