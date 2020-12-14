package com.peter.command;

import com.peter.execute.ExecuteDebug;
import com.peter.execute.domains.ExecuteDomains;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CommandDomains implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandDomains.class);

    @Override
    public String commandName() {
        return "domains";
    }

    @Override
    public int priority() {
        return 6;
    }

    private int limit = -1;

    @Override
    public void execute(String[] args) throws Exception {
        if (!leggal(args)) {
            System.out.print(getHelp());
            return;
        }
        ExecuteDomains.listAllDomains(limit);
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        int i = 1;
        int status = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "-limit":
                    i ++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -limit");
                        return false;
                    }
                    try {
                        limit = Integer.parseInt(args[i]);
                    } catch (NumberFormatException e) {
                        logger.info("Limit " + args[i] + " is not an integer");
                        return false;
                    }
                    if (limit < 0) {
                        logger.info("Limit " + limit + " must be large than 0");
                        return false;
                    }
                    break;
                case "-d":
                    ExecuteDebug.setAllLoggerLevelDebug();
                    break;
                default:
                    logger.info("Unknown parameter " + args[i]);
                    return false;
            }

            // important
            i ++;
        }
        return true;
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
