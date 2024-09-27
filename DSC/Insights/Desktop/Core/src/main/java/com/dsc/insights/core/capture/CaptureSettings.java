package com.dsc.insights.core.capture;

import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;

import java.io.File;

public class CaptureSettings
{
    private String UID;
    private String typeKey;
    private String name;

    private String accountKey;
    private String projectUID;
    private String platformUID;
    private String assessmentUID;

    public CaptureSettings()
    {

    }

    public CaptureSettings(File fileSettings)
    {
        String jsonSettings = FileCommon.getInstance().readFile(fileSettings, "UTF-8");
        if (jsonSettings != null && jsonSettings.isBlank() == false)
        {
            CaptureSettings loadSettings = (CaptureSettings) JSONCommon.getInstance().deserialize(jsonSettings, CaptureSettings.class);
            if (loadSettings != null)
            {
                UID = loadSettings.getUID();
                typeKey = loadSettings.getTypeKey();
                name = loadSettings.getName();
                accountKey = loadSettings.getAccountKey();
                projectUID = loadSettings.getProjectUID();
                platformUID = loadSettings.getPlatformUID();
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

    public String getTypeKey()
    {
        return typeKey;
    }

    public void setTypeKey(String typeKey)
    {
        this.typeKey = typeKey;
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

    public String getPlatformUID()
    {
        return platformUID;
    }

    public void setPlatformUID(String platformUID)
    {
        this.platformUID = platformUID;
    }

    public String getAssessmentUID()
    {
        return assessmentUID;
    }

    public void setAssessmentUID(String assessmentUID)
    {
        this.assessmentUID = assessmentUID;
    }
}
