package com.dsc.insights.core.account;

import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;

import java.io.File;

public class AccountSettings
{
    private String UID;
    private String key;
    private String name;

    public AccountSettings()
    {

    }

    public AccountSettings(File fileSettings)
    {
        String jsonSettings = FileCommon.getInstance().readFile(fileSettings, "UTF-8");
        if (jsonSettings != null && jsonSettings.isBlank() == false)
        {
            AccountSettings loadSettings = (AccountSettings) JSONCommon.getInstance().deserialize(jsonSettings, AccountSettings.class);
            if (loadSettings != null)
            {
                UID = loadSettings.getUID();
                key = loadSettings.getKey();
                name = loadSettings.getName();
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
}
