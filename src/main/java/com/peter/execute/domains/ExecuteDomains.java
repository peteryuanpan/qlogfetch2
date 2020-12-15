package com.peter.execute.domains;

import com.peter.execute.ExecuteInfo;
import com.peter.model.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecuteDomains {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteDomains.class);

    public static void listAllDomains(int limit) throws Exception {
        Account account = ExecuteInfo.readAccountJsonFile();
        if (account == null)
            return;
        String className = ExecuteDomains.class.getName() + account.getSource().getUpperCaseName();
        Class<?> clazz = Class.forName(className);
        ExecuteDomainsInterface instance = (ExecuteDomainsInterface) clazz.newInstance();
        Response response = instance.execute(account, limit);
        assert response != null;
        if (response.code == 200) {
            if (response.domains != null) {
                for (String s : response.domains)
                    System.out.println(s);
            } else
                logger.info("Get response code 200 but domain list is null");
        }
        else {
            logger.info("Failed to list all domains");
            logger.info("code: " + response.code);
            logger.info("message: " + response.message);
            logger.info("You can add '-d' to print debug log");
        }
    }

    public static class Response {
        int code;
        String message;
        String[] domains;
    }
}
