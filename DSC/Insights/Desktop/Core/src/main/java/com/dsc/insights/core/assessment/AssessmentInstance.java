package com.dsc.insights.core.assessment;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.capture.CaptureInstance;
import com.dsc.insights.core.capture.CaptureSettings;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AssessmentInstance
{
    private final File assessmentDirectory;
    private final String capturesDir;

    private AssessmentSettings settings;
    private Image imageLogo;

    private Map<String, CaptureSettings> captures;
    private Map<String, CaptureInstance> captureInstances;

    public AssessmentInstance(File inDirectory)
    {
        assessmentDirectory = inDirectory;
        capturesDir = assessmentDirectory.getPath() + "/captures/";

        File fileConfig = new File(assessmentDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (AssessmentSettings) JSONCommon.getInstance().deserialize(jsonContent, AssessmentSettings.class);
        
        loadCaptures();
    }

    public AssessmentSettings getSettings()
    {
        return settings;
    }

    public File getAssessmentDirectory()
    {
        return assessmentDirectory;
    }

    public String getCapturesDir()
    {
        return capturesDir;
    }

    public boolean save()
    {
        String jsonConfig = JSONCommon.getInstance().serialize(settings);
        File fileConfig = new File(assessmentDirectory, "settings.json");
        return FileCommon.getInstance().writeFile(fileConfig, jsonConfig, "UTF-8");
    }

    public Image getLogo()
    {
        if (imageLogo != null)
        {
            return imageLogo;
        }

        File fileLogo = new File(assessmentDirectory, "logo.png");
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
            imageLogo = AppImages.ASSESSMENT.getImage();
        }

        return imageLogo;
    }

    public boolean saveLogo(BufferedImage biLogo)
    {
        if (biLogo != null)
        {
            File fileLogo = new File(assessmentDirectory, "logo.png");

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

    public Map<String, CaptureSettings> getCaptures()
    {
        return captures;
    }

    public CaptureInstance getCapture(String captureUID)
    {
        if (captureInstances.containsKey(captureUID) == false)
        {
            String captureDirPath = capturesDir + captureUID + "/";
            File captureDir = new File(captureDirPath);
            if (captureDir.exists() == false)
            {
                return null;
            }

            CaptureInstance instance = new CaptureInstance(captureDir);
            captureInstances.put(captureUID, instance);
        }
        return captureInstances.get(captureUID);
    }

    public boolean createCapture(CaptureSettings capture)
    {
        if (captures.containsKey(capture.getUID()))
        {
            return true;
        }

        capture.setAccountKey(settings.getAccountKey());
        capture.setProjectUID(settings.getProjectUID());
        capture.setPlatformUID(settings.getPlatformUID());
        capture.setAssessmentUID(settings.getUID());

        String captureSettingsPath = capturesDir + "/" + capture.getUID() + "/settings.json";

        FileCommon.getInstance().makeDirectory(captureSettingsPath);

        File fileCaptureSettings = new File(captureSettingsPath);
        String jsonSettings = JSONCommon.getInstance().serialize(capture);

        FileCommon.getInstance().writeFile(fileCaptureSettings, jsonSettings, "UTF-8");

        captures.put(capture.getUID(), capture);

        return true;
    }

    public boolean deleteCapture(String captureUID)
    {
        String captureDirPath = capturesDir + captureUID + "/";
        File captureDir = new File(captureDirPath);
        if (captureDir.exists() == false)
        {
            return false;
        }

        boolean deleted = FileCommon.getInstance().deleteDirectory(captureDirPath);
        if (deleted == true)
        {
            captures.remove(captureUID);
            captureInstances.remove(captureUID);
        }
        return deleted;
    }

    private void loadCaptures()
    {
        captures = new ConcurrentHashMap<>();
        captureInstances = new ConcurrentHashMap<>();

        File dirCaptures = new File(capturesDir);
        if (dirCaptures.exists() == false)
        {
            FileCommon.getInstance().makeDirectory(dirCaptures);
        }

        for (File nextCaptureDir : dirCaptures.listFiles())
        {
            if (nextCaptureDir.isDirectory() == true)
            {
                File fileSettings = new File(nextCaptureDir, "settings.json");
                if (fileSettings.exists() == true)
                {
                    CaptureSettings addSettings = new CaptureSettings(nextCaptureDir);

                    captures.put(addSettings.getUID(), addSettings);
                }
            }
        }
    }

}
