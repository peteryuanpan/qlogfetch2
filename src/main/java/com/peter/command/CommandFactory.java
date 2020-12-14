package com.peter.command;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class CommandFactory {

    private static final Logger logger = LoggerFactory.getLogger(CommandFactory.class);

    private static List<CommandInterface> listCommand;

    public static List<CommandInterface> getListCommand() {
        if (listCommand == null) {
            String basePackages = "com.peter.command";
            Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(basePackages));
            Set<Class<? extends CommandInterface>> setClass = reflections.getSubTypesOf(CommandInterface.class);
            listCommand = new ArrayList<>();
            setClass.forEach(clazz -> {
                try {
                    listCommand.add(getCommand(clazz));
                } catch (Exception e) {
                    logger.error(e.getClass().getName() + " " + e.getMessage());
                }
            });
            Collections.sort(listCommand, new Comparator<CommandInterface>() {
                @Override
                public int compare(CommandInterface command1, CommandInterface command2) {
                    return command1.priority() - command2.priority();
                }
            });
        }
        return listCommand;
    }

    private static CommandInterface getCommand(Class clazz) throws Exception {
        try {
            Method getInstance = clazz.getDeclaredMethod("getInstance");
            return (CommandInterface) getInstance.invoke(null);
        } catch (Exception e) {
            Constructor<? extends CommandInterface> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        }
    }
}
