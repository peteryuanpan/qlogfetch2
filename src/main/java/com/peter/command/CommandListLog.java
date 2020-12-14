package com.peter.command;

import com.peter.execute.ExecuteDebug;
import com.peter.execute.listlog.ExecuteListLog;
import com.peter.model.ListLog;
import com.peter.util.DateUtil;
import com.peter.util.DomainUtil;
import com.peter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandListLog implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandListLog.class);

    @Override
    public String commandName() {
        return "listlog";
    }

    @Override
    public int priority() {
        return 3;
    }

    private final ListLog listLog = new ListLog();

    @Override
    public void execute(String[] args) throws Exception {
        if (!leggal(args)) {
            System.out.print(getHelp());
            return;
        }
        ExecuteListLog.listLogUrls(listLog);
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        int i = 1;
        int status = 0;
        while (i < args.length) {
            switch(args[i]) {
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
                        listLog.setFromDate(date1);
                        listLog.setToDate(date2);
                    }
                    status |= 0x001;
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
                        sb.append((sb.toString().isEmpty() ? "" : ";") + domain);
                    }
                    listLog.setDomains(list.toArray(new String[0]));
                    listLog.setDomainStr(sb.toString());
                    status |= 0x010;
                    break;
                case "-d":
                    ExecuteDebug.setAllLoggerLevelDebug();
                    status |= 0x100;
                    break;
                default:
                    logger.info("Unknown parameter " + args[i]);
                    return false;
            }

            // important
            i ++;
        }
        if ((status & 0x001) == 0)
            logger.info("Missing parameter -date");
        if ((status & 0x010) == 0)
            logger.info("Missing parameter -domains");
        return (status & 0x011) == 0x011;
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
