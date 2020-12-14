package com.peter.command;

import com.peter.execute.ExecuteInfo;
import com.peter.model.Account;
import com.peter.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CommandInfo implements CommandInterface {

    private static final Logger logger = LoggerFactory.getLogger(CommandInfo.class);

    @Override
    public String commandName() {
        return "info";
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public void execute(String[] args) throws IOException {
        if (!leggal(args)) {
            System.out.print(getHelp());
            return;
        }
        Account account = ExecuteInfo.readAccountJsonFile();
        System.out.print(Json.encode(account) + "\n");
    }

    private boolean leggal(String[] args) {
        if (args == null || args.length < 1 || !commandName().equals(args[0]))
            return false;
        if (args.length > 1) {
            logger.info("Length of parameters too long");
            return false;
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
