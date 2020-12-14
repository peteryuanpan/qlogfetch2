package com.peter.command;

import com.peter.execute.ExecuteReg;
import com.peter.model.Account;
import com.peter.model.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CommandReg implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandReg.class);

    @Override
    public String commandName() {
        return "reg";
    }

    @Override
    public int priority() {
        return 1;
    };

    private final Account account = new Account();

    @Override
    public void execute(String[] args) throws IOException {
        if (!leggal(args)) {
            System.out.print(getHelp());
            return;
        }
        ExecuteReg.writeAccountToFile(account);
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        int i = 1;
        int status = 0;
        while (i < args.length) {
            switch(args[i]) {
                case "-ak":
                    i ++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -ak");
                        return false;
                    }
                    account.setAk(args[i]);
                    status |= 0x001;
                    break;
                case "-sk":
                    i ++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -sk");
                        return false;
                    }
                    account.setSk(args[i]);
                    status |= 0x010;
                    break;
                case "-source":
                    i ++;
                    if (i >= args.length) {
                        logger.info("Missing value of parameter -source");
                        return false;
                    }
                    Source source = Source.getSource(args[i]);
                    if (source == null) {
                        logger.info("Unknown value of parameter -source " + args[i]);
                        return false;
                    }
                    account.setSource(source);
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
            logger.info("Missing parameter -ak");
        if ((status & 0x010) == 0)
            logger.info("Missing parameter -sk");
        if ((status & 0x100) == 0)
            account.setSource(Source.qiniu); // default source is qiniu
        return (status & 0x011) == 0x011;
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
