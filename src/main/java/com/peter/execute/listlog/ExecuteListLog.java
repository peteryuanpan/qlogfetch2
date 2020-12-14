package com.peter.execute.listlog;

import com.peter.execute.ExecuteInfo;
import com.peter.model.Account;
import com.peter.model.ListLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteListLog {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteListLog.class);

    public static void listLogUrls(ListLog listLog) throws Exception {
        Account account = ExecuteInfo.readAccountJsonFile();
        if (account == null)
            return;
        String className = ExecuteListLog.class.getName() + account.getSource().getUpperCaseName();
        Class<?> clazz = Class.forName(className);
        ExecuteListLogInterface instance = (ExecuteListLogInterface) clazz.newInstance();
        Response response = instance.execute(account, listLog);
        assert response != null;
        if (response.code == 200) {
            if (response.urls != null) {
                for (String s : response.urls)
                    System.out.println(s);
            } else
                logger.info("Get response code 200 but domain list is null");
        }
        else {
            logger.info("Failed to list log urls of domain");
            logger.info("code: " + response.code);
            logger.info("message: " + response.message);
            logger.info("You can add '-d' to print debug log");
        }
    }

    public static class Response {

        int code;
        String message;
        String[] urls;

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String[] getUrls() {
            return urls;
        }
    }
}
