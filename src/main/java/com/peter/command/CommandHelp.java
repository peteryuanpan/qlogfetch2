package com.peter.command;

import java.io.IOException;

public class CommandHelp implements CommandInterface {

    private CommandHelp() {
    }

    private static final CommandHelp instance = new CommandHelp();

    public static CommandHelp getInstance() {
        return instance;
    }

    @Override
    public String commandName() {
        return "-h";
    }

    @Override
    public int priority() {
        return 0;
    }

    @Override
    public void execute(String[] args) throws IOException {
        System.out.print(getHelp());
    }

    private static String help;

    public String getHelp() throws IOException {
        if (help == null)
            help = CommandInterface.super.getHelp();
        return help;
    }
}
