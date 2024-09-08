package com.dsc.insights.core.account;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.project.ProjectInstance;
import com.dsc.insights.core.project.ProjectSettings;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountInstance
{
    private File accountDirectory;

    private AccountSettings settings;
    private Image imageLogo;

    private String projectsDir;
    private Map<String, ProjectSettings> projects;
    private Map<String, ProjectInstance> projectInstances;

    private boolean loaded = false;

    public AccountInstance(File inDirectory)
    {
        accountDirectory = inDirectory;

        if (accountDirectory.isDirectory() == false)
        {
            return;
        }

        File fileSettings = new File(accountDirectory, "settings.json");
        if (fileSettings.exists() == false)
        {
            return;
        }

        String jsonSettings = FileCommon.getInstance().readFile(fileSettings, "UTF-8");
        settings = (AccountSettings) JSONCommon.getInstance().deserialize(jsonSettings, AccountSettings.class);
        if (settings == null)
        {
            return;
        }

        projectsDir = accountDirectory + "/projects/";

        loadProjects();

        loaded = true;
    }

    public boolean isLoaded()
    {
        return loaded;
    }

    public AccountSettings getSettings()
    {
        return settings;
    }

    public boolean saveSettings()
    {
        String jsonContent = JSONCommon.getInstance().serialize(settings);
        File fileSettings = new File(accountDirectory, "settings.json");
        FileCommon.getInstance().writeFile(fileSettings, jsonContent, "UTF-8");

        //Ensure the projects sub-directory is created
        FileCommon.getInstance().makeDirectory(projectsDir);

        return true;
    }

    public File getLogoFile()
    {
        File fileLogo = new File(accountDirectory, "logo.png");

        return fileLogo;
    }

    public Image getLogo()
    {
        if (imageLogo != null)
        {
            return imageLogo;
        }

        File fileLogo = new File(accountDirectory, "logo.png");
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
            imageLogo = AppImages.ACCOUNT.getImage();
        }

        return imageLogo;
    }

    public boolean saveLogo(BufferedImage biLogo)
    {
        if (biLogo != null)
        {
            File fileLogo = new File(accountDirectory, "logo.png");

            BufferedImage biScaled = DSCI.getInstance().getGradedScaler().scaleGraded(biLogo, 256);
            boolean saved = ImageCommon.getInstance().writeImage(biScaled, "PNG", fileLogo);
            if (saved == true)
            {
                imageLogo = SwingFXUtils.toFXImage(biScaled, null);
                return true;
            }
        }

        return false;
    }

    public Map<String, ProjectSettings> getProjects()
    {
        return projects;
    }

    public ProjectInstance getProject(String projectUID)
    {
        //If this instance is not already open, create a new instance
        if (projectInstances.containsKey(projectUID) == false)
        {
            String projectDirPath = projectsDir + projectUID + "/";
            File projectDir = new File(projectDirPath);
            if (projectDir.exists() == false)
            {
                return null;
            }

            ProjectInstance instance = new ProjectInstance(projectDir);
            projectInstances.put(projectUID, instance);
        }
        return projectInstances.get(projectUID);
    }

    public ProjectInstance createProject(ProjectSettings projectSettings)
    {
        String projectUID = projectSettings.getUID();
        if (projects.containsKey(projectUID))
        {
            return getProject(projectUID);
        }

        //Set the account UID
        projectSettings.setAccountKey(settings.getKey());

        String projectDirPath = projectsDir + projectUID + "/";
        File projectDir = new File(projectDirPath);
        FileCommon.getInstance().makeDirectory(projectDir);

        File fileSettings = new File(projectDir, "settings.json");
        String jsonSettings = JSONCommon.getInstance().serialize(projectSettings);
        FileCommon.getInstance().writeFile(fileSettings, jsonSettings, "UTF-8");

        projects.put(projectUID, projectSettings);

        ProjectInstance instance = new ProjectInstance(projectDir);
        projectInstances.put(projectUID, instance);

        return instance;
    }

    public boolean deleteProject(String projectUID)
    {
        String projectDirPath = projectsDir + projectUID + "/";
        File projectDir = new File(projectDirPath);
        if (projectDir.exists() == false)
        {
            return false;
        }

        boolean deleted = FileCommon.getInstance().deleteDirectory(projectDirPath);
        if (deleted == true)
        {
            projects.remove(projectUID);
            projectInstances.remove(projectUID);
        }
        return deleted;
    }

    private void loadProjects()
    {
        projects = new ConcurrentHashMap<>();
        projectInstances = new ConcurrentHashMap<>();

        File fileProjectsDirectory = new File(projectsDir);
        if (fileProjectsDirectory.exists() == false)
        {
            FileCommon.getInstance().makeDirectory(fileProjectsDirectory);
        }

        for (File nextExtDir: fileProjectsDirectory.listFiles())
        {
            if (nextExtDir.isDirectory() == true)
            {
                File fileProjectSettings = new File(nextExtDir, "settings.json");
                if (fileProjectSettings.exists() == true)
                {
                    ProjectSettings projectSettings = new ProjectSettings(fileProjectSettings);

                    projects.put(projectSettings.getUID(), projectSettings);
                }
            }
        }
    }
}
