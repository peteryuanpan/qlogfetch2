package com.peter.command;

import com.peter.util.StringUtil;

import java.util.Arrays;

public class CommandFilter {

    public static String[] filter(String[] args) {
        return Arrays.stream(args).filter(CommandFilter::filter).toArray(String[]::new);
    }

    private static boolean filter(String arg) {
        if (StringUtil.isEmpty(arg))
            return false;
        if (arg.startsWith("-X")) // pass JVM args
            return false;
        return true;
    }
}
