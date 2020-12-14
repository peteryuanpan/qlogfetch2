package com.peter.execute;

import com.peter.util.StringMap;
import okhttp3.*;

import java.io.IOException;

public class ExecuteHttp {

    private ExecuteHttp() {
    }

    private static final ExecuteHttp instance = new ExecuteHttp();

    public static ExecuteHttp getInstance() {
        return instance;
    }

    private final OkHttpClient client = new OkHttpClient();

    private Response send(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    public Response get(String url) throws IOException {
        return get(getRequest(url));
    }

    public Response get(String url, StringMap headers) throws IOException {
        return get(getRequest(url, headers));
    }

    public Response get(Request request) throws IOException {
        return send(request);
    }

    public Request getRequest(String url) {
        return getRequest(url, null);
    }

    public Request getRequest(String url, StringMap headers) {
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null)
            headers.forEach((key, value) -> builder.addHeader(key, value.toString()));
        return builder.build();
    }

    public Response post(String url, StringMap body, CONTENT_TYPE contentType) throws IOException {
        return post(postRequest(url, body, contentType));
    }

    public Response post(String url, StringMap headers, StringMap body, CONTENT_TYPE contentType) throws IOException {
        return post(postRequest(url, headers, body, contentType));
    }

    public Response post(Request request) throws IOException {
        return send(request);
    }

    public Request postRequest(String url, StringMap body, CONTENT_TYPE contentType) {
        return postRequest(url, null, body, contentType);
    }

    public Request postRequest(String url, StringMap headers, StringMap body, CONTENT_TYPE contentType) {
        RequestBody requestBody;
        switch (contentType) {
            case FORM:
                requestBody = RequestBody.create(MediaType.parse(contentType.value), body.formString());
                break;
            case JSON:
            default:
                requestBody = RequestBody.create(MediaType.parse(contentType.value), body.jsonString());
                break;
        }
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        builder.method("POST", requestBody);
        if (headers != null)
            headers.forEach((key, value) -> builder.addHeader(key, value.toString()));
        return builder.build();
    }

    public enum CONTENT_TYPE {
        JSON("application/json"),
        FORM("application/x-www-form-urlencoded");

        private String value;

        CONTENT_TYPE(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
