package com.peter.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HelpReader {

    private static final Logger logger = LoggerFactory.getLogger(HelpReader.class);

    private static final Map<String, String> help = new HashMap<>();

    static {
        URL resource = HelpReader.class.getClassLoader().getResource("help.md");
        assert resource != null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(resource.openStream()));
            boolean contentIn = false;
            String className = "";
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if ("```".equals(line)) {
                    if (!contentIn)
                        sb = new StringBuilder();
                    else
                        help.put(className, sb.toString());
                    contentIn = !contentIn;
                } else {
                    if (!contentIn)
                        className = line;
                    else
                        sb.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            logger.error(e.getClass().getName() + " " + e.getMessage());
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error(e.getClass().getName() + " " + e.getMessage());
                }
            }
        }
    }

    public static String getHelp(String className) {
        return help.get(className);
    }
}
