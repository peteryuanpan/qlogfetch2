package com.peter.execute.domains;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20180510.DescribeUserDomainsRequest;
import com.aliyuncs.cdn.model.v20180510.DescribeUserDomainsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.peter.model.Account;
import com.peter.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档：https://help.aliyun.com/document_detail/91188.html
 */
public class ExecuteDomainsAliyun implements ExecuteDomainsInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteDomainsAliyun.class);

    @Override
    public ExecuteDomains.Response execute(Account account, int limit) {
        ExecuteDomains.Response resp = new ExecuteDomains.Response();
        DefaultProfile profile = DefaultProfile.getProfile("", account.getAk(), account.getSk());
        IAcsClient client = new DefaultAcsClient(profile);

        List<String> domains = new ArrayList<>();
        int pageNumber = 1;

        while (pageNumber <= 100000) { // pageNumber: [1, 100000]
            DescribeUserDomainsResponse response;
            try {
                DescribeUserDomainsRequest request = new DescribeUserDomainsRequest();
                request.setPageSize(50); // pageSize: [1, 50]
                request.setPageNumber(pageNumber);
                logger.debug(request.toString());
                response = client.getAcsResponse(request);
                logger.debug(Json.encode(response));
            } catch (ClientException e) { // See DefaultAcsClient#parseAcsResponse
                resp.code = 999;
                resp.message = Json.encode(e);
                break;
            }
            resp.code = 200;
            resp.message = "OK";

            List<DescribeUserDomainsResponse.PageData> pageDatas = response.getDomains();
            if (pageDatas == null || pageDatas.size() == 0)
                break;

            for (DescribeUserDomainsResponse.PageData pageData : pageDatas) {
                domains.add(pageData.getDomainName());
            }
            if (limit > 0 && domains.size() >= limit) {
                logger.debug("Request is not finished and stopped because at least " + limit + " domains has gotten");
                domains = domains.subList(0, limit);
                break;
            }
            pageNumber ++;
        }

        if (resp.code == 200)
            resp.domains = domains.toArray(new String[0]);

        return resp;
    }
}
