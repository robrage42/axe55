package com.dsc.insights;

import javafx.scene.image.Image;

import java.net.URL;

public enum AppImages
{
    ACCOUNT("icons/account.png"),
    ADD("icons/add.png"),
    ASSESSMENT("icons/assessment.png"),
    BACKGROUND("images/background.jpg"),
    CAPTURE("icons/capture.png"),
    CLOSE("icons/close.png"),
    COPY("icons/copy.png"),
    DELETE("icons/delete.png"),
    EDIT("icons/edit.png"),
    LOGO("logo.png"),
    MATRIX("icons/matrix.png"),
    MAXIMIZE("icons/maximize.png"),
    MINIMIZE("icons/minimize.png"),
    OPEN("icons/open.png"),
    PLATFORM("icons/platform.png"),
    PROJECT("icons/project.png"),
    SETTINGS("icons/settings.png"),
    STATUS_PENDING("icons/status_pending.png"),
    STATUS_RUNNING("icons/status_running.png"),
    STATUS_COMPLETE("icons/status_complete.png"),
    STATUS_ERROR("icons/status_error.png"),
    PUFF("images/puff.png");

    private final String path;
    private final URL imageURL;
    private Image image;

    AppImages(String inPath)
    {
        path = "/com/dsc/insights/" + inPath;
        imageURL = getClass().getResource(path);
    }

    public String getPath()
    {
        return path;
    }

    public URL getURL()
    {
        return imageURL;
    }

    public Image getImage()
    {
        if (image == null)
        {
            image = new Image(path);
        }
        return image;
    }
}
