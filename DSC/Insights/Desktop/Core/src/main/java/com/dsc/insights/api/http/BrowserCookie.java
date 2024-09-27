package com.dsc.insights.api.http;

import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

public class BrowserCookie
{
    private String name;
    private String value;
    private String path;
    private String domain;
    private String expires;
    private boolean httpOnly;
    private boolean secure;

    public BrowserCookie()
    {

    }

    public void parse(LinkedTreeMap mapCookie)
    {
        name = GSONCommon.getString(mapCookie, "name");
        value = GSONCommon.getString(mapCookie, "value");
        path = GSONCommon.getString(mapCookie, "path");
        domain = GSONCommon.getString(mapCookie, "domain");
        expires = GSONCommon.getString(mapCookie, "expires");
        httpOnly = GSONCommon.getBoolean(mapCookie, "httpOnly");
        secure = GSONCommon.getBoolean(mapCookie, "secure");
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getDomain()
    {
        return domain;
    }

    public void setDomain(String domain)
    {
        this.domain = domain;
    }

    public String getExpires()
    {
        return expires;
    }

    public void setExpires(String expires)
    {
        this.expires = expires;
    }

    public boolean isHttpOnly()
    {
        return httpOnly;
    }

    public void setHttpOnly(boolean httpOnly)
    {
        this.httpOnly = httpOnly;
    }

    public boolean isSecure()
    {
        return secure;
    }

    public void setSecure(boolean secure)
    {
        this.secure = secure;
    }
}
