package com.dsc.insights.core.matrix;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;

public class MatrixInstance
{
    private final File matrixDirectory;

    private MatrixSettings settings;
    private Image imageLogo;

    public MatrixInstance(File inDirectory)
    {
        matrixDirectory = inDirectory;

        File fileConfig = new File(matrixDirectory, "settings.json");
        String jsonContent = FileCommon.getInstance().readFile(fileConfig, "UTF-8");
        settings = (MatrixSettings) JSONCommon.getInstance().deserialize(jsonContent, MatrixSettings.class);
    }

    public MatrixSettings getSettings()
    {
        return settings;
    }

    public boolean save()
    {
        String jsonConfig = JSONCommon.getInstance().serialize(settings);
        File fileConfig = new File(matrixDirectory, "settings.json");
        return FileCommon.getInstance().writeFile(fileConfig, jsonConfig, "UTF-8");
    }

    public Image getLogo()
    {
        if (imageLogo != null)
        {
            return imageLogo;
        }

        File fileLogo = new File(matrixDirectory, "logo.png");
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
            imageLogo = AppImages.MATRIX.getImage();
        }

        return imageLogo;
    }

    public boolean saveLogo(BufferedImage biLogo)
    {
        if (biLogo != null)
        {
            File fileLogo = new File(matrixDirectory, "logo.png");

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
}
