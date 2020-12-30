package com.peter;

import com.peter.command.CommandFilter;
import com.peter.command.CommandHelp;
import com.peter.command.CommandInterface;
import com.peter.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        try {
            getCommand(args).execute(CommandFilter.filter(args));
        } catch (Throwable e) {
            logger.error(e.getClass().getName() + " " + e.getMessage());
        }
    }

    private static CommandInterface getCommand(String[] args) {
        if (args != null && args.length >= 1) {
            Iterator<CommandInterface> iterator = CommandFactory.getListCommand().iterator();
            while (iterator.hasNext()) {
                CommandInterface command = iterator.next();
                if (command.commandName().equals(args[0])) {
                    return command;
                }
            }
        }
        return CommandHelp.getInstance();
    }
}
