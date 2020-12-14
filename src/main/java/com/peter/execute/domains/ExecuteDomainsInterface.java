package com.peter.execute.domains;

import com.peter.model.Account;

public interface ExecuteDomainsInterface {

    ExecuteDomains.Response execute(Account account, int limit) throws Exception;
}
