package com.dsc.insights.core.matrix;

import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;

import java.io.File;

public class MatrixSettings
{
    private String UID;
    private String key;
    private String name;

    private String accountKey;
    private String projectUID;

    public MatrixSettings()
    {

    }

    public MatrixSettings(File fileSettings)
    {
        String jsonSettings = FileCommon.getInstance().readFile(fileSettings, "UTF-8");
        if (jsonSettings != null && jsonSettings.isBlank() == false)
        {
            MatrixSettings loadSettings = (MatrixSettings) JSONCommon.getInstance().deserialize(jsonSettings, MatrixSettings.class);
            if (loadSettings != null)
            {
                UID = loadSettings.getUID();
                key = loadSettings.getKey();
                name = loadSettings.getName();
                accountKey = loadSettings.getAccountKey();
                projectUID = loadSettings.getProjectUID();
            }
        }
    }

    public String getUID()
    {
        return UID;
    }

    public void setUID(String UID)
    {
        this.UID = UID;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAccountKey()
    {
        return accountKey;
    }

    public void setAccountKey(String accountKey)
    {
        this.accountKey = accountKey;
    }

    public String getProjectUID()
    {
        return projectUID;
    }

    public void setProjectUID(String projectUID)
    {
        this.projectUID = projectUID;
    }

}
