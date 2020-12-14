package com.peter.execute.listlog;

import com.peter.model.Account;
import com.peter.model.ListLog;

public interface ExecuteListLogInterface {

    ExecuteListLog.Response execute(Account account, ListLog listlog) throws Exception;
}
