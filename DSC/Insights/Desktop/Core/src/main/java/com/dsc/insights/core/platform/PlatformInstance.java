package com.dsc.insights.core.platform;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.assessment.AssessmentInstance;
import com.dsc.insights.core.assessment.AssessmentSettings;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlatformInstance
{
    private final File platformDirectory;
    private final String assessmentsDir;

    private PlatformSettings settings;
    private Image imageLogo;

    private Map<String, AssessmentSettings> assessments;
    private Map<String, AssessmentInstance> assessmentInstances;

    public PlatformInstance(File inDirectory)
    {
        platformDirectory = inDirectory;
        assessmentsDir = platformDirectory.getPath() + "/assessments/";

        File fileConfig = new File(platformDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (PlatformSettings) JSONCommon.getInstance().deserialize(jsonContent, PlatformSettings.class);

        loadPlatforms();
    }

    public PlatformSettings getSettings()
    {
        return settings;
    }

    public boolean save()
    {
        String jsonConfig = JSONCommon.getInstance().serialize(settings);
        File fileConfig = new File(platformDirectory, "settings.json");
        return FileCommon.getInstance().writeFile(fileConfig, jsonConfig, "UTF-8");
    }

    public Image getLogo()
    {
        if (imageLogo != null)
        {
            return imageLogo;
        }

        File fileLogo = new File(platformDirectory, "logo.png");
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
            File fileLogo = new File(platformDirectory, "logo.png");

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

    public Map<String, AssessmentSettings> getAssessments()
    {
        return assessments;
    }

    public AssessmentInstance getAssessment(String assessmentUID)
    {
        if (assessmentInstances.containsKey(assessmentUID) == false)
        {
            String assessmentDirPath = assessmentsDir + assessmentUID + "/";
            File assesstmentDir = new File(assessmentDirPath);
            if (assesstmentDir.exists() == false)
            {
                return null;
            }

            AssessmentInstance instance = new AssessmentInstance(assesstmentDir);
            assessmentInstances.put(assessmentUID, instance);
        }
        return assessmentInstances.get(assessmentUID);
    }

    public boolean createAssessment(AssessmentSettings assessment)
    {
        if (assessments.containsKey(assessment.getUID()))
        {
            return true;
        }

        assessment.setAccountKey(settings.getAccountKey());
        assessment.setProjectUID(settings.getProjectUID());
        assessment.setPlatformUID(settings.getUID());

        String assessmentSettingsPath = assessmentsDir + "/" + assessment.getUID() + "/settings.json";

        FileCommon.getInstance().makeDirectory(assessmentSettingsPath);

        File fileAssessmentSettings = new File(assessmentSettingsPath);
        String jsonSettings = JSONCommon.getInstance().serialize(assessment);

        FileCommon.getInstance().writeFile(fileAssessmentSettings, jsonSettings, "UTF-8");

        assessments.put(assessment.getUID(), assessment);

        return true;
    }

    public boolean deleteAssessment(String assessmentUID)
    {
        String assessmentDirPath = assessmentsDir + assessmentUID + "/";
        File assessmentDir = new File(assessmentDirPath);
        if (assessmentDir.exists() == false)
        {
            return false;
        }

        boolean deleted = FileCommon.getInstance().deleteDirectory(assessmentDirPath);
        if (deleted == true)
        {
            assessments.remove(assessmentUID);
            assessmentInstances.remove(assessmentUID);
        }
        return deleted;
    }

    private void loadPlatforms()
    {
        assessments = new ConcurrentHashMap<>();
        assessmentInstances = new ConcurrentHashMap<>();

        File dirAssessments = new File(assessmentsDir);
        if (dirAssessments.exists() == false)
        {
            FileCommon.getInstance().makeDirectory(dirAssessments);
        }

        for (File nextAssessmentDir : dirAssessments.listFiles())
        {
            if (nextAssessmentDir.isDirectory() == true)
            {
                File fileSettings = new File(nextAssessmentDir, "settings.json");
                if (fileSettings.exists() == true)
                {
                    AssessmentSettings addSettings = new AssessmentSettings(nextAssessmentDir);

                    assessments.put(addSettings.getUID(), addSettings);
                }
            }
        }
    }

}
