package com.dsc.insights.core.capture;

public enum CaptureType
{
    WEB_SESSION("Web Session"),
    NET_CAPTURE("Network Capture"),
    SW_ANALYSIS("Software Analysis");

    private final String display;

    CaptureType(String inDisplay)
    {
        display = inDisplay;
    }

    public String getDisplay()
    {
        return display;
    }
}
