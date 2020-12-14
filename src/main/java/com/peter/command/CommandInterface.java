package com.peter.command;

import com.peter.util.HelpReader;

import java.io.IOException;

public interface CommandInterface {

    String commandName();

    int priority();

    void execute(String[] args) throws Exception;

    default String getHelp() throws IOException {
        return HelpReader.getHelp(this.getClass().getName());
    }
}
