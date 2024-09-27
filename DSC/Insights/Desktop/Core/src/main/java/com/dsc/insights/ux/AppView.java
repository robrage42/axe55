package com.dsc.insights.ux;

import com.pxg.jfx.fxml.FXMLInstance;

import java.util.ResourceBundle;

public enum AppView
{
    ACCOUNT_CREATE("/com/dsc/insights/ux/account/AccountCreate.fxml"),
    ACCOUNT_VIEWER("/com/dsc/insights/ux/account/AccountViewer.fxml"),
    ACCOUNT_PROFILE("/com/dsc/insights/ux/account/AccountProfile.fxml"),

    APP_LAUNCH("/com/dsc/insights/ux/AppLaunch.fxml"),
    APP_SETTINGS("/com/dsc/insights/ux/AppSettings.fxml"),
    APP_LOGIN("/com/dsc/insights/ux/AppLogin.fxml"),
    APP_HOME("/com/dsc/insights/ux/AppHome.fxml"),
    APP_USER_SETTINGS("/com/dsc/insights/ux/user/AppUserSettings.fxml"),

    ASSESSMENT_CREATE("/com/dsc/insights/ux/account/project/platform/assessment/AssessmentCreate.fxml"),
    ASSESSMENT_VIEWER("/com/dsc/insights/ux/account/project/platform/assessment/AssessmentViewer.fxml"),

    CAPTURE_VIEWER("/com/dsc/insights/ux/account/project/platform/assessment/capture/CaptureViewer.fxml"),

    MATRIX_CREATE("/com/dsc/insights/ux/account/project/matrix/MatrixCreate.fxml"),
    MATRIX_VIEWER("/com/dsc/insights/ux/account/project/matrix/MatrixViewer.fxml"),

    PLATFORM_CREATE("/com/dsc/insights/ux/account/project/platform/PlatformCreate.fxml"),
    PLATFORM_VIEWER("/com/dsc/insights/ux/account/project/platform/PlatformViewer.fxml"),

    PROJECT_CREATE("/com/dsc/insights/ux/account/project/ProjectCreate.fxml"),
    PROJECT_VIEWER("/com/dsc/insights/ux/account/project/ProjectViewer.fxml");

    private final String FXMLPath;

    AppView(String inFXML)
    {
        FXMLPath = inFXML;
    }

    public String getFXMLPath()
    {
        return FXMLPath;
    }

    public FXMLInstance create(ResourceBundle resources)
    {
        return new FXMLInstance(FXMLPath, resources);
    }

}
