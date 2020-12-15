package com.peter.execute.listlog;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.peter.execute.ExecuteHttp;
import com.peter.model.Account;
import com.peter.model.ListLog;
import com.peter.util.DateUtil;
import com.peter.util.Json;
import com.peter.util.StringMap;
import com.qiniu.util.Auth;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.peter.execute.ExecuteHttp.CONTENT_TYPE.JSON;

/**
 * 接口文档：https://developer.qiniu.com/fusion/api/1226/download-the-log
 */
public class ExecuteListLogQiniu implements ExecuteListLogInterface {

    private static final Logger logger = LoggerFactory.getLogger(ExecuteListLogQiniu.class);

    private static final String url0 = "http://fusion.qiniuapi.com/v2/tune/log/list";

    @Override
    public ExecuteListLog.Response execute(Account account, ListLog listlog) throws Exception {
        ExecuteListLog.Response resp = new ExecuteListLog.Response();
        Auth auth = Auth.create(account.getAk(), account.getSk());
        ExecuteHttp http = ExecuteHttp.getInstance();
        List<String> urls = new ArrayList<>();

        Date fromDate = listlog.getFromDate();
        Date toDate = listlog.getToDate();
        logger.debug("fromDate: " + DateUtil.format1(fromDate));
        logger.debug("toDate: " + DateUtil.format1(toDate));

        Date nowDate = (Date) fromDate.clone();
        while (true) {
            logger.debug("nowDate: " + DateUtil.format1(nowDate));

            StringMap reqbody = new StringMap();
            reqbody.put("day", DateUtil.format1(nowDate));
            reqbody.put("domains", listlog.getDomainStr());

            String token = (String) auth.authorization(url0, reqbody.jsonString().getBytes(), JSON.getValue()).get("Authorization");
            StringMap headers = new StringMap();
            headers.put("Authorization", token);
            logger.debug("request headers: " + headers.toString());

            Request request = http.postRequest(url0, headers, reqbody, JSON);
            logger.debug(request.toString());

            Response response = http.post(request);
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
                for (String domain : listlog.getDomains()) {
                    JsonElement datas = respbody.data.get(domain);
                    if (datas != null) {
                        datas.getAsJsonArray().forEach(d -> {
                            ResponseBodyData data = Json.decode(d.toString(), ResponseBodyData.class);
                            if (data != null)
                                urls.add(data.url);
                        });
                    }
                }
            }

            if (DateUtil.format1(nowDate).equals(DateUtil.format1(toDate)))
                break;
            nowDate = DateUtil.incrementOneDayAndGet(nowDate);
        }

        if (resp.code == 200) {
            resp.urls = urls.stream().filter(url -> {
                String[] sp = url.split("_");
                assert sp.length >= 2;
                Date date = DateUtil.parseDate(sp[1]);
                assert date != null;
                return !date.before(fromDate) && !date.after(toDate);
            }).toArray(String[]::new);
        }

        return resp;
    }

    static class ResponseBody {
        private int code;
        private String error;
        private JsonObject data;
    }

    static class ResponseBodyData {
        private String name;
        private String size;
        private String mtime;
        private String url;
    }
}
