package com.peter.execute;

import com.peter.model.Account;
import com.peter.util.Constants;
import com.peter.util.Json;
import com.peter.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class ExecuteInfo {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteInfo.class);

    private static Account account;

    public static Account readAccountJsonFile() {
        if (account == null)
            account = readAccountJsonFile0();
        if (account == null)
            throw new NullPointerException();
        return account;
    }

    private static Account readAccountJsonFile0() {
        File file = new File(Constants.AccountJsonFilePath);
        try {
            if (!file.exists()) {
                logger.info("File " + file.getAbsolutePath() + " does not exists");
                logger.info("Please use 'reg' to set access key and secret key [and source] first");
                return null;
            }
            InputStream inputStream = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            byte[] b = new byte[1024];
            int len;
            while ((len = inputStream.read(b)) != -1) {
                sb.append(new String(b, 0, len));
            }
            inputStream.close();
            Account account = Json.decode(sb.toString(), Account.class);
            if (account == null || account.getSource() == null ||
                StringUtil.isEmpty(account.getAk()) || StringUtil.isEmpty(account.getSk()))
            {
                logger.info("Account Info Data Error");
                logger.info("Please use 'reg' to set access key and secret key [and source] again");
                return null;
            }
            return account;
        } catch (Exception e) {
            logger.info("Failed to read account info from " + file.getAbsolutePath());
            logger.error(e.getClass().getName() + " " + e.getMessage());
            return null;
        }
    }
}
