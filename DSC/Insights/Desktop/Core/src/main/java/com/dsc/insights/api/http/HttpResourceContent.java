package com.dsc.insights.api.http;

import com.google.gson.internal.LinkedTreeMap;
import com.pxg.dio.GSONCommon;

public class HttpResourceContent
{
    private boolean partial;
    private int size;
    private String mimeType;
    private String text;

//    private HashSet hashSet;

    public HttpResourceContent()
    {

    }

    public void parse(boolean inPartial, LinkedTreeMap mapContent)
    {
        partial = inPartial;
        size = GSONCommon.getInteger(mapContent, "size");
        mimeType = GSONCommon.getString(mapContent, "mimeType");
        if (partial == false)
        {
            text = GSONCommon.getString(mapContent, "text");
//            hashSet = Hasher.createSet(text);
        }
        else
        {
            text = "";
//            hashSet = null;
        }

    }

    public boolean isPartial()
    {
        return partial;
    }

    public void setPartial(boolean partial)
    {
        this.partial = partial;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getMimeType()
    {
        return mimeType;
    }

    public void setMimeType(String mimeType)
    {
        this.mimeType = mimeType;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

//    public HashSet getHashSet()
//    {
//        return hashSet;
//    }
//
//    public void setHashSet(HashSet hashSet)
//    {
//        this.hashSet = hashSet;
//    }
}
