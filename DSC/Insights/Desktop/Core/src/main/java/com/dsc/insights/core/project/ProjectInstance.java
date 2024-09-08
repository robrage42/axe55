package com.dsc.insights.core.project;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.platform.PlatformInstance;
import com.dsc.insights.core.platform.PlatformSettings;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectInstance
{
    private final File projectDirectory;
    private final String platformsDir;

    private ProjectSettings settings;
    private Image imageLogo;

    private Map<String, PlatformSettings> platforms;
    private Map<String, PlatformInstance> platformInstances;

    public ProjectInstance(File inDirectory)
    {
        projectDirectory = inDirectory;
        platformsDir = projectDirectory.getPath() + "/platforms/";

        File fileConfig = new File(projectDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (ProjectSettings) JSONCommon.getInstance().deserialize(jsonContent, ProjectSettings.class);

        loadPlatforms();
    }

    public ProjectSettings getSettings()
    {
        return settings;
    }

    public boolean save()
    {
        String jsonConfig = JSONCommon.getInstance().serialize(settings);
        File fileConfig = new File(projectDirectory, "settings.json");
        return FileCommon.getInstance().writeFile(fileConfig, jsonConfig, "UTF-8");
    }

    public Image getLogo()
    {
        if (imageLogo != null)
        {
            return imageLogo;
        }

        File fileLogo = new File(projectDirectory, "logo.png");
        if (fileLogo.exists() == true)
        {
            BufferedImage biLogo = ImageCommon.getInstance().bufferImage(fileLogo);
            if (biLogo != null)
            {
                imageLogo = SwingFXUtils.toFXImage(biLogo, null);
            }
        }

        if (imageLogo == null)
        {
            imageLogo = AppImages.PROJECT.getImage();
        }

        return imageLogo;
    }

    public boolean saveLogo(BufferedImage biLogo)
    {
        if (biLogo != null)
        {
            File fileLogo = new File(projectDirectory, "logo.png");

            BufferedImage biScaled = DSCI.getInstance().getGradedScaler().scaleGraded(biLogo, 256);
            boolean saved = ImageCommon.getInstance().writeImage(biScaled, "PNG", fileLogo);
            if (saved)
            {
                imageLogo = SwingFXUtils.toFXImage(biScaled, null);
            }

            return saved;
        }

        return false;
    }

    public Map<String, PlatformSettings> getPlatforms()
    {
        return platforms;
    }

    public PlatformInstance getPlatform(String platformUID)
    {
        if (platformInstances.containsKey(platformUID) == false)
        {
            String platformDirPath = platformsDir + platformUID + "/";
            File platformDir = new File(platformDirPath);
            if (platformDir.exists() == false)
            {
                return null;
            }

            PlatformInstance instance = new PlatformInstance(platformDir);
            platformInstances.put(platformUID, instance);
        }
        return platformInstances.get(platformUID);
    }

    public boolean createPlatform(PlatformSettings platform)
    {
        if (platforms.containsKey(platform.getUID()))
        {
            return true;
        }

        platform.setAccountKey(settings.getAccountKey());
        platform.setProjectUID(settings.getUID());

        String platformDir = platformsDir + platform.getUID() + "/";
        FileCommon.getInstance().makeDirectory(platformDir);

        File filePlatformSettings = new File(platformDir, "settings.json");
        String jsonSettings = JSONCommon.getInstance().serialize(platform);

        FileCommon.getInstance().writeFile(filePlatformSettings, jsonSettings, "UTF-8");

        platforms.put(platform.getUID(), platform);

        return true;
    }

    public boolean deletePlatform(String platformUID)
    {
        String platformDirPath = platformsDir + platformUID + "/";
        File platformDir = new File(platformDirPath);
        if (platformDir.exists() == false)
        {
            return false;
        }

        boolean deleted = FileCommon.getInstance().deleteDirectory(platformDirPath);
        if (deleted == true)
        {
            platforms.remove(platformUID);
            platformInstances.remove(platformUID);
        }
        return deleted;
    }

    private void loadPlatforms()
    {
        platforms = new ConcurrentHashMap<>();
        platformInstances = new ConcurrentHashMap<>();

        File dirPlatforms = new File(platformsDir);
        if (dirPlatforms.exists() == false)
        {
            FileCommon.getInstance().makeDirectory(dirPlatforms);
        }

        for (File nextPlatformDir : dirPlatforms.listFiles())
        {
            if (nextPlatformDir.isDirectory() == true)
            {
                File fileSettings = new File(nextPlatformDir, "settings.json");
                if (fileSettings.exists() == true)
                {
                    PlatformSettings addSettings = new PlatformSettings(fileSettings);

                    platforms.put(addSettings.getUID(), addSettings);
                }
            }
        }
    }

}
