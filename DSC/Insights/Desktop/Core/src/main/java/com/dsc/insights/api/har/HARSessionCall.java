package com.dsc.insights.api.har;

import com.dsc.insights.api.http.HttpResourceRequest;
import com.dsc.insights.api.http.HttpResourceResponse;
import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

public class HARSessionCall
{
    private int callID;
    private String pageURL;
    private String resourceType;
    private String startedDateTime;
    private double time;
    private String serverIPAddress;
    private HttpResourceRequest request;
    private HttpResourceResponse response;

    public HARSessionCall()
    {
        super();
    }

    public void parse(LinkedTreeMap mapCall)
    {
        serverIPAddress = GSONCommon.getString(mapCall, "serverIPAddress");
        if (serverIPAddress.startsWith("[") && serverIPAddress.endsWith("]"))
        {
            serverIPAddress = serverIPAddress.substring(1, serverIPAddress.length() - 1);
        }
        resourceType = GSONCommon.getString(mapCall, "_resourceType");
        startedDateTime = GSONCommon.getString(mapCall, "startedDateTime");
        time = GSONCommon.getDouble(mapCall, "time");

        LinkedTreeMap entryRequest = GSONCommon.getLinkedTreeMap(mapCall, "request");

        request = new HttpResourceRequest();
        request.parse(entryRequest);

        LinkedTreeMap entryResponse = GSONCommon.getLinkedTreeMap(mapCall, "response");
        response = new HttpResourceResponse();
        response.parse(entryResponse);
    }

    public int getCallID()
    {
        return callID;
    }

    public void setCallID(int callID)
    {
        this.callID = callID;
    }

    public String getPageURL()
    {
        return pageURL;
    }

    public void setPageURL(String pageURL)
    {
        this.pageURL = pageURL;
    }

    public String getResourceType()
    {
        return resourceType;
    }

    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }

    public String getStartedDateTime()
    {
        return startedDateTime;
    }

    public void setStartedDateTime(String startedDateTime)
    {
        this.startedDateTime = startedDateTime;
    }

    public double getTime()
    {
        return time;
    }

    public void setTime(double time)
    {
        this.time = time;
    }

    public String getServerIPAddress()
    {
        return serverIPAddress;
    }

    public void setServerIPAddress(String serverIPAddress)
    {
        this.serverIPAddress = serverIPAddress;
    }

    public HttpResourceRequest getRequest()
    {
        return request;
    }

    public void setRequest(HttpResourceRequest request)
    {
        this.request = request;
    }

    public HttpResourceResponse getResponse()
    {
        return response;
    }

    public void setResponse(HttpResourceResponse response)
    {
        this.response = response;
    }
}
