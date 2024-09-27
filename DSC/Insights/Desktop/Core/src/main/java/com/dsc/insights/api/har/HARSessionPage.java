package com.dsc.insights.api.har;

import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

public class HARSessionPage
{
    private String id;
    private String url;
    private String startedTime;

    public HARSessionPage()
    {

    }

    public void parse(LinkedTreeMap mapPage)
    {
        id = GSONCommon.getString(mapPage, "id");
        url = GSONCommon.getString(mapPage, "title");
        startedTime = GSONCommon.getString(mapPage, "startedDateTime");
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getStartedTime()
    {
        return startedTime;
    }

    public void setStartedTime(String startedTime)
    {
        this.startedTime = startedTime;
    }
}
