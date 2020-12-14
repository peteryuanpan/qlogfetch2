package com.peter.execute;

import com.peter.model.Account;
import com.peter.util.Constants;
import com.peter.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class ExecuteReg {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteReg.class);

    public static void writeAccountToFile(Account account) {
        File file = new File(Constants.AccountJsonFilePath);
        try {
            file.getParentFile().mkdirs();
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(Json.encode(account).getBytes());
            outputStream.flush();
            outputStream.close();
            logger.info("Write account info to " + file.getPath());
        } catch (IOException e) {
            logger.info("Failed to write account info to " + file.getAbsolutePath());
            logger.error(e.getClass().getName() + " " + e.getMessage());
        }
    }
}
