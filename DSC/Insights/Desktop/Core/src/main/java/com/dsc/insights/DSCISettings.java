package com.dsc.insights;

public class DSCISettings
{
    private String settingsDirectory;

    public DSCISettings()
    {

    }

    public void load(DSCISettings existing)
    {
        settingsDirectory = existing.settingsDirectory;
    }

    public String getSettingsDirectory()
    {
        return settingsDirectory;
    }

    public void setSettingsDirectory(String settingsDirectory)
    {
        this.settingsDirectory = settingsDirectory;
    }
}
