package com.dsc.insights.core.sdo;

import java.util.Map;

public class HTTPCall
{
    private String method;
    private String URL;
    private String hostname;
    private String domain;
    private int port;

    private Map<String, String> requestHeaders;
    private Map<String, String> responseHeaders;
    private String body;
    private String contentType;
    private String contentEncoding;
    private String contentLanguage;
    private String contentLength;

    private long sentAt;
    private long receivedAt;
    private long processTime;

    private String requestHash;
    private String contentHash;

    private transient String matchKey;

    public HTTPCall()
    {

    }

    public String getMatchKey()
    {
        if (matchKey != null)
        {
            return matchKey;
        }

        matchKey = requestHash + "-" + contentHash;

        return matchKey;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }

    public String getURL()
    {
        return URL;
    }

    public void setURL(String URL)
    {
        this.URL = URL;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public Map<String, String> getRequestHeaders()
    {
        return requestHeaders;
    }

    public void setRequestHeaders(Map<String, String> requestHeaders)
    {
        this.requestHeaders = requestHeaders;
    }

    public Map<String, String> getResponseHeaders()
    {
        return responseHeaders;
    }

    public void setResponseHeaders(Map<String, String> responseHeaders)
    {
        this.responseHeaders = responseHeaders;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getContentType()
    {
        return contentType;
    }

    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    public String getContentEncoding()
    {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding)
    {
        this.contentEncoding = contentEncoding;
    }

    public String getContentLanguage()
    {
        return contentLanguage;
    }

    public void setContentLanguage(String contentLanguage)
    {
        this.contentLanguage = contentLanguage;
    }

    public String getContentLength()
    {
        return contentLength;
    }

    public void setContentLength(String contentLength)
    {
        this.contentLength = contentLength;
    }

    public long getSentAt()
    {
        return sentAt;
    }

    public void setSentAt(long sentAt)
    {
        this.sentAt = sentAt;
    }

    public long getReceivedAt()
    {
        return receivedAt;
    }

    public void setReceivedAt(long receivedAt)
    {
        this.receivedAt = receivedAt;
    }

    public long getProcessTime()
    {
        return processTime;
    }

    public void setProcessTime(long processTime)
    {
        this.processTime = processTime;
    }

    public String getRequestHash()
    {
        return requestHash;
    }

    public void setRequestHash(String requestHash)
    {
        this.requestHash = requestHash;
    }

    public String getContentHash()
    {
        return contentHash;
    }

    public void setContentHash(String contentHash)
    {
        this.contentHash = contentHash;
    }
}
