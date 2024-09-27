package com.dsc.insights.api.har;

import java.io.File;
import java.util.Map;

public class HARSession
{
    private File fileInput;
    private Map<String, HARSessionPage> pages;
    private Map<Integer, HARSessionCall> calls;

    public HARSession()
    {

    }

    public File getFileInput()
    {
        return fileInput;
    }

    public void setFileInput(File fileInput)
    {
        this.fileInput = fileInput;
    }

    public Map<String, HARSessionPage> getPages()
    {
        return pages;
    }

    public void setPages(Map<String, HARSessionPage> pages)
    {
        this.pages = pages;
    }

    public Map<Integer, HARSessionCall> getCalls()
    {
        return calls;
    }

    public void setCalls(Map<Integer, HARSessionCall> calls)
    {
        this.calls = calls;
    }
}
