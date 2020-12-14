package com.peter.execute.listlog;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.cdn.model.v20180510.DescribeCdnDomainLogsRequest;
import com.aliyuncs.cdn.model.v20180510.DescribeCdnDomainLogsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.peter.model.Account;
import com.peter.model.ListLog;
import com.peter.util.DateUtil;
import com.peter.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口文档：https://help.aliyun.com/document_detail/91154.html
 */
public class ExecuteListLogAliyun implements ExecuteListLogInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteListLogAliyun.class);

    @Override
    public ExecuteListLog.Response execute(Account account, ListLog listlog) throws Exception {
        ExecuteListLog.Response resp = new ExecuteListLog.Response();
        DefaultProfile profile = DefaultProfile.getProfile("", account.getAk(), account.getSk());
        IAcsClient client = new DefaultAcsClient(profile);

        Date fromDate = listlog.getFromDate();
        Date toDate = listlog.getToDate();
        if (fromDate.toInstant().toString().equals(toDate.toInstant().toString()))
            toDate = DateUtil.incrementOneHourAndGet(toDate);
        logger.debug("fromDate: " + fromDate.toInstant().toString());
        logger.debug("toDate: " + toDate.toInstant().toString());

        List<String> urls = new ArrayList<>();
        for (String domain : listlog.getDomains()) {
            logger.debug("begin list logs of domain " + domain);
            long pageNumber = 1;
            while (pageNumber < Integer.MAX_VALUE) { // pageNumber: [1, ++]
                DescribeCdnDomainLogsResponse response;
                try {
                    DescribeCdnDomainLogsRequest request = new DescribeCdnDomainLogsRequest();
                    request.setDomainName(domain);
                    request.setStartTime(fromDate.toInstant().toString());
                    request.setEndTime(toDate.toInstant().toString());
                    request.setPageSize(1000L); // pageSize: [1, 1000]
                    request.setPageNumber(pageNumber);
                    logger.debug(request.toString());
                    response = client.getAcsResponse(request);
                    logger.debug(Json.encode(response));
                } catch (ClientException e) {
                    resp.code = 999;
                    resp.message = Json.encode(e);
                    break;
                }
                resp.code = 200;
                resp.message = "OK";

                List<DescribeCdnDomainLogsResponse.DomainLogDetail> domainLogDetails = response.getDomainLogDetails();
                if (domainLogDetails == null || domainLogDetails.size() == 0)
                    break;

                boolean br = false;
                for (DescribeCdnDomainLogsResponse.DomainLogDetail domainLogDetail : domainLogDetails) {
                    if ("".equals(domainLogDetail.getDomainName()))
                        br = true;
                    for (DescribeCdnDomainLogsResponse.DomainLogDetail.LogInfoDetail logInfo : domainLogDetail.getLogInfos()) {
                        String url = logInfo.getLogPath();
                        if (!url.startsWith("http://") && !url.startsWith("https://"))
                            url = "http://" + url;
                        urls.add(url);
                    }
                }
                if (br)
                    break;
                pageNumber ++;
            }
        }

        if (resp.code == 200)
            resp.urls = urls.toArray(new String[0]);

        return resp;
    }
}
