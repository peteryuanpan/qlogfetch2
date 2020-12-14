package com.peter.execute.downlog;

import com.peter.model.DownLog;

public interface ExecuteDownLogInterface {

    ExecuteDownLog.Response execute(DownLog downLog, String[] urls) throws Exception;
}
