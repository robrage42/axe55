package com.dsc.insights.api.http;

import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResourceResponse
{
    private int status;
    private String statusText;
    private String httpVersion;
    private Map<String, String> headers;
    private List<BrowserCookie> cookies;
    private String redirectURL;
    private int headersSize;
    private int bodySize;
    private HttpResourceContent content;
    private int transferSize;

    private transient String textVersion;

    public HttpResourceResponse()
    {

    }

    public void parse(LinkedTreeMap mapResponse)
    {
        status = Double.valueOf(GSONCommon.getDouble(mapResponse, "status")).intValue();
        statusText = GSONCommon.getString(mapResponse, "statusText");
        httpVersion = GSONCommon.getString(mapResponse, "httpVersion");
        redirectURL = GSONCommon.getString(mapResponse, "redirectURL");
        headersSize = Double.valueOf(GSONCommon.getDouble(mapResponse, "headersSize")).intValue();
        bodySize = Double.valueOf(GSONCommon.getDouble(mapResponse, "bodySize")).intValue();
        transferSize = Double.valueOf(GSONCommon.getDouble(mapResponse, "_transferSize")).intValue();

        headers = new HashMap<>();
        ArrayList<LinkedTreeMap> listHeaders = GSONCommon.getArrayList(mapResponse, "headers");
        for (LinkedTreeMap nextHeader: listHeaders)
        {
            String name = GSONCommon.getString(nextHeader, "name");
            String value = GSONCommon.getString(nextHeader, "value");
            headers.put(name, value);
        }

        cookies = new ArrayList<>();
        ArrayList<LinkedTreeMap> listCookies = GSONCommon.getArrayList(mapResponse, "cookies");
        for (LinkedTreeMap nextCookie: listCookies)
        {
            BrowserCookie cookie = new BrowserCookie();
            cookie.parse(nextCookie);

            cookies.add(cookie);
        }

        boolean partial = (status == 206);

        LinkedTreeMap mapContent = GSONCommon.getLinkedTreeMap(mapResponse, "content");
        content = new HttpResourceContent();
        content.parse(partial, mapContent);
    }

    public String toText()
    {
        if (textVersion != null)
        {
            return textVersion;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("Status: " + status + "\n");
        sb.append("Status Text: " + statusText + "\n");
        sb.append("Redirect URL: " + redirectURL + "\n");
        sb.append("HTTP Version: " + httpVersion + "\n");
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
        sb.append("Body Size: " + bodySize + "\n");
        sb.append("Content\n");
        sb.append("Mime Type: " + content.getMimeType() + "\n");
        sb.append("Size: " + content.getSize() + "\n");

        String mimeType = content.getMimeType().toLowerCase();
        if (mimeType.contains("audio/") == false
                && mimeType.contains("image/") == false
                && mimeType.contains("video/") == false
                && mimeType.contains("application/epub+zip") == false
                && mimeType.contains("application/java-archive") == false
                && mimeType.contains("application/octet-stream") == false
                && mimeType.contains("application/ogg") == false
                && mimeType.contains("application/vnd.apple.installer+xml") == false
                && mimeType.contains("application/vnd.rar") == false
                && mimeType.contains("application/x-bzip") == false
                && mimeType.contains("application/x-cdf") == false
                && mimeType.contains("application/x-tar") == false
                && mimeType.contains("application/x-zip") == false
                && mimeType.contains("application/x-7z") == false
                && mimeType.contains("application/gzip") == false
                && mimeType.contains("application/zip") == false
        )
        {
            sb.append("Body: " + content.getText() + "\n");
        }
        sb.append("Transfer Size: " + transferSize + "\n");

        textVersion = sb.toString();
        return textVersion;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getStatusText()
    {
        return statusText;
    }

    public void setStatusText(String statusText)
    {
        this.statusText = statusText;
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

    public List<BrowserCookie> getCookies()
    {
        return cookies;
    }

    public void setCookies(List<BrowserCookie> cookies)
    {
        this.cookies = cookies;
    }

    public String getRedirectURL()
    {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL)
    {
        this.redirectURL = redirectURL;
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

    public HttpResourceContent getContent()
    {
        return content;
    }

    public void setContent(HttpResourceContent content)
    {
        this.content = content;
    }

    public int getTransferSize()
    {
        return transferSize;
    }

    public void setTransferSize(int transferSize)
    {
        this.transferSize = transferSize;
    }
}
