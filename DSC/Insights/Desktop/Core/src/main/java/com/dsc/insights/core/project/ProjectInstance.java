package com.dsc.insights.core.project;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.matrix.MatrixInstance;
import com.dsc.insights.core.matrix.MatrixSettings;
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
    private final String matrixDir;
    private final String platformsDir;

    private ProjectSettings settings;
    private Image imageLogo;

    private Map<String, MatrixSettings> matrixes;
    private Map<String, MatrixInstance> matrixInstances;

    private Map<String, PlatformSettings> platforms;
    private Map<String, PlatformInstance> platformInstances;

    public ProjectInstance(File inDirectory)
    {
        projectDirectory = inDirectory;
        platformsDir = projectDirectory.getPath() + "/platforms/";
        matrixDir = projectDirectory.getPath() + "/matrix/";

        File fileConfig = new File(projectDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (ProjectSettings) JSONCommon.getInstance().deserialize(jsonContent, ProjectSettings.class);

        loadMatrixes();
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

    public Map<String, MatrixSettings> getMatrixes()
    {
        return matrixes;
    }

    public MatrixInstance getMatrix(String matrixUID)
    {
        if (matrixInstances.containsKey(matrixUID) == false)
        {
            String matrixDirPath = matrixDir + matrixUID + "/";
            File matrixDir = new File(matrixDirPath);
            if (matrixDir.exists() == false)
            {
                return null;
            }

            MatrixInstance instance = new MatrixInstance(matrixDir);
            matrixInstances.put(matrixUID, instance);
        }
        return matrixInstances.get(matrixUID);
    }

    public boolean createMatrix(MatrixSettings matrix)
    {
        if (matrixes.containsKey(matrix.getUID()))
        {
            return true;
        }

        matrix.setAccountKey(settings.getAccountKey());
        matrix.setProjectUID(settings.getUID());

        String matrixSettingsPath = matrixDir + "/" + matrix.getUID() + "/settings.json";

        FileCommon.getInstance().makeDirectory(matrixSettingsPath);

        File fileMatrixSettings = new File(matrixSettingsPath);
        String jsonSettings = JSONCommon.getInstance().serialize(matrix);

        FileCommon.getInstance().writeFile(fileMatrixSettings, jsonSettings, "UTF-8");

        matrixes.put(matrix.getUID(), matrix);

        return true;
    }

    public boolean deleteMatrix(String matrixUID)
    {
        String matrixDirPath = matrixDir + matrixUID + "/";
        File matrixDir = new File(matrixDirPath);
        if (matrixDir.exists() == false)
        {
            return false;
        }

        boolean deleted = FileCommon.getInstance().deleteDirectory(matrixDirPath);
        if (deleted == true)
        {
            matrixes.remove(matrixUID);
            matrixInstances.remove(matrixUID);
        }
        return deleted;
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

    private void loadMatrixes()
    {
        matrixes = new ConcurrentHashMap<>();
        matrixInstances = new ConcurrentHashMap<>();

        File dirMatrixes = new File(matrixDir);
        if (dirMatrixes.exists() == false)
        {
            FileCommon.getInstance().makeDirectory(dirMatrixes);
        }

        for (File nextMatrixDir : dirMatrixes.listFiles())
        {
            if (nextMatrixDir.isDirectory() == true)
            {
                File fileSettings = new File(nextMatrixDir, "settings.json");
                if (fileSettings.exists() == true)
                {
                    MatrixSettings addSettings = new MatrixSettings(nextMatrixDir);

                    matrixes.put(addSettings.getUID(), addSettings);
                }
            }
        }
    }
}
