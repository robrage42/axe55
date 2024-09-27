package com.dsc.insights.api.http;

import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpResourceRequest
{
    private String method;
    private String url;
    private String httpVersion;
    private Map<String, String> headers;
    private Map<String, String> queryConditions;
    private List<BrowserCookie> cookies;
    private int headersSize;
    private int bodySize;

    private transient String textVersion;

    public HttpResourceRequest()
    {

    }

    public void parse(LinkedTreeMap mapRequest)
    {
        method = GSONCommon.getString(mapRequest, "method");
        url = GSONCommon.getString(mapRequest, "url");
        httpVersion = GSONCommon.getString(mapRequest, "httpVersion");
        headersSize = Double.valueOf(GSONCommon.getDouble(mapRequest, "headersSize")).intValue();
        bodySize = Double.valueOf(GSONCommon.getDouble(mapRequest, "bodySize")).intValue();

        headers = new LinkedHashMap<>();
        ArrayList<LinkedTreeMap> listHeaders = GSONCommon.getArrayList(mapRequest, "headers");
        for (LinkedTreeMap nextHeader: listHeaders)
        {
            String name = GSONCommon.getString(nextHeader, "name");
            String value = GSONCommon.getString(nextHeader, "value");
            headers.put(name, value);
        }

        queryConditions = new LinkedHashMap<>();
        ArrayList<LinkedTreeMap> listQueries = GSONCommon.getArrayList(mapRequest, "queryString");
        for (LinkedTreeMap nextQuery: listQueries)
        {
            String name = GSONCommon.getString(nextQuery, "name");
            String value = GSONCommon.getString(nextQuery, "value");
            queryConditions.put(name, value);
        }

        cookies = new ArrayList<>();
        ArrayList<LinkedTreeMap> listCookies = GSONCommon.getArrayList(mapRequest, "cookies");
        for (LinkedTreeMap nextCookie: listCookies)
        {
            BrowserCookie cookie = new BrowserCookie();
            cookie.parse(nextCookie);

            cookies.add(cookie);
        }
    }

    public String toText()
    {
        if (textVersion != null)
        {
            return textVersion;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Method: " + method + "\n");
        sb.append("HTTP Version: " + httpVersion + "\n");
        sb.append("URL: " + url + "\n");
        sb.append("Body Size: " + bodySize + "\n");
        sb.append("Query Strings\n");
        for (String nextQueryKey: queryConditions.keySet())
        {
            String queryValue = queryConditions.get(nextQueryKey);
            sb.append(nextQueryKey + "=" + queryValue + "\n");
        }
        sb.append("Headers Size: " + headersSize + "\n");
        sb.append("Headers\n");
        for (String nextKey: headers.keySet())
        {
            String nextValue = headers.get(nextKey);
            sb.append(nextKey + " = " + nextValue + "\n");
        }
        sb.append("Cookies\n");
        for (BrowserCookie nextCookie: cookies)
        {
            sb.append(nextCookie.getName() + " = " + nextCookie.getValue() + ", " + nextCookie.getDomain() + ", " + nextCookie.getPath() + ", " + nextCookie.getExpires() + "\n");
        }

        textVersion = sb.toString();
        return textVersion;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion)
    {
        this.httpVersion = httpVersion;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public void setHeaders(Map<String, String> headers)
    {
        this.headers = headers;
    }

    public Map<String, String> getQueryConditions()
    {
        return queryConditions;
    }

    public void setQueryConditions(Map<String, String> queryConditions)
    {
        this.queryConditions = queryConditions;
    }

    public List<BrowserCookie> getCookies()
    {
        return cookies;
    }

    public void setCookies(List<BrowserCookie> cookies)
    {
        this.cookies = cookies;
    }

    public int getHeadersSize()
    {
        return headersSize;
    }

    public void setHeadersSize(int headersSize)
    {
        this.headersSize = headersSize;
    }

    public int getBodySize()
    {
        return bodySize;
    }

    public void setBodySize(int bodySize)
    {
        this.bodySize = bodySize;
    }
}
