package com.dsc.insights.core.capture;

import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import javafx.scene.image.Image;

import java.io.File;

public class CaptureInstance
{
    private final File captureDirectory;

    private CaptureSettings settings;
    private Image imageLogo;

    public CaptureInstance(File inDirectory)
    {
        captureDirectory = inDirectory;

        File fileConfig = new File(captureDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (CaptureSettings) JSONCommon.getInstance().deserialize(jsonContent, CaptureSettings.class);
    }

    public CaptureSettings getSettings()
    {
        return settings;
    }

    public boolean save()
    {
        String jsonConfig = JSONCommon.getInstance().serialize(settings);
        File fileConfig = new File(captureDirectory, "settings.json");
        return FileCommon.getInstance().writeFile(fileConfig, jsonConfig, "UTF-8");
    }
}
