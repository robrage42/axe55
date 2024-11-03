package com.dsc.insights.api.har;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class HARReader
{
    private File fileInput;

    public Map<String, HARSessionPage> pages;
    public Map<Integer, HARSessionCall> calls;

    public HARReader()
    {

    }

    public HARSession load(File inFile)
    {
        fileInput = inFile;

        pages = new LinkedHashMap<>();
        calls = new LinkedHashMap<>();

        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(fileInput));

            Gson gson = new Gson();
            LinkedTreeMap content = gson.fromJson(reader, LinkedTreeMap.class);

            pages = new HashMap<>();
            LinkedTreeMap contentLog = GSONCommon.getLinkedTreeMap(content, "log");
            ArrayList<LinkedTreeMap> contentPages = GSONCommon.getArrayList(contentLog, "pages");

            for (LinkedTreeMap nextPage : contentPages)
            {
                HARSessionPage page = new HARSessionPage();
                page.parse(nextPage);

                pages.put(page.getId(), page);
            }

            ArrayList<LinkedTreeMap> contentEntries = GSONCommon.getArrayList(contentLog, "entries");

            int callID = 0;
            for (LinkedTreeMap nextCall : contentEntries)
            {
                callID++;

                parseCall(callID, nextCall);
            }

            HARSession session = new HARSession();
            session.setFileInput(fileInput);
            session.setPages(pages);
            session.setCalls(calls);

            return session;
        }
        catch (Exception e)
        {
            System.out.println("HARReader::load::ERROR::" + e.getMessage());
            e.printStackTrace();
        }

        return null;
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

    private void parseCall(int callID, LinkedTreeMap nextCall)
    {
        try
        {
            String pageID = GSONCommon.getString(nextCall, "pageref");
            HARSessionPage page = pages.get(pageID);
            String pageURL = "";
            if (page != null)
            {
                pageURL = page.getUrl();
            }

            HARSessionCall call = new HARSessionCall();
            call.setCallID(callID);
            call.setPageURL(pageURL);
            call.parse(nextCall);

            calls.put(callID, call);
        }
        catch (Exception e)
        {
            System.out.println("HARReader::parseCall::ERROR::" + e.getMessage());
            e.printStackTrace();
        }
    }
}
