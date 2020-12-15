package com.peter.execute.domains;

import com.peter.execute.ExecuteHttp;
import com.peter.model.Account;
import com.peter.util.Json;

import com.peter.util.StringMap;
import com.peter.util.StringUtil;
import com.qiniu.util.Auth;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口文档：https://developer.qiniu.com/fusion/api/4246/the-domain-name#9
 */
public class ExecuteDomainsQiniu implements ExecuteDomainsInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteDomainsQiniu.class);

    private static final String url0 = "http://api.qiniu.com/domain?limit=1000";

    @Override
    public ExecuteDomains.Response execute(Account account, int limit) throws IOException {
        ExecuteDomains.Response resp = new ExecuteDomains.Response();
        Auth auth = Auth.create(account.getAk(), account.getSk());
        ExecuteHttp http = ExecuteHttp.getInstance();

        List<String> domains = new ArrayList<>();
        String marker = null;
        do {
            String url = url0 + (StringUtil.isEmpty(marker) ? "" : "&marker=" + marker);

            String token = (String) auth.authorizationV2(url).get("Authorization");
            StringMap headers = new StringMap();
            headers.put("Authorization", token);
            logger.debug("request headers: " + headers.toString());

            Request request = http.getRequest(url, headers);
            logger.debug(request.toString());

            Response response = http.get(request);
            assert response != null && response.body() != null;
            String body = response.body().string();
            logger.debug(response.toString());
            logger.debug("response body: " + body);

            resp.code = response.code();
            resp.message = response.message();
            if (resp.code != 200) {
                resp.message = response.message() + ", body: " + body;
                break;
            }

            ResponseBody respbody = Json.decode(body, ResponseBody.class);
            if (respbody != null) {
                marker = respbody.marker;
                if (respbody.domains != null) {
                    for (int i = 0; i < respbody.domains.length; i++)
                        domains.add(respbody.domains[i].name);
                }
            }
            if (limit > 0 && domains.size() >= limit) {
                logger.debug("Request is not finished and stopped because at least " + limit + " domains has gotten");
                domains = domains.subList(0, limit);
                break;
            }

        } while(!StringUtil.isEmpty(marker));

        if (resp.code == 200)
            resp.domains = domains.toArray(new String[0]);

        return resp;
    }

    static class ResponseBody {
        private String marker;
        private Domain[] domains;
        static class Domain {
            private String name;
        }
    }
}
