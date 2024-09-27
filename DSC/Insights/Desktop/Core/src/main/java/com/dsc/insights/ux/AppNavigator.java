package com.dsc.insights.ux;

import com.dsc.insights.DSCI;

import java.util.HashMap;
import java.util.Map;

public class AppNavigator
{
    public static void showAccountCreate()
    {
        Map params = new HashMap<>();

        DSCI.getInstance().getUXInstance().navigateTo(AppView.ACCOUNT_CREATE.name(), AppView.ACCOUNT_CREATE.name(), params, true);
    }

    public static void showAccountViewer(String accountKey)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.ACCOUNT_VIEWER.name(), AppView.ACCOUNT_VIEWER.name(), params, true);
    }

    public static void showAccountProfile(String accountKey)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.ACCOUNT_PROFILE.name(), AppView.ACCOUNT_PROFILE.name(), params, true);
    }

    public static void showAppLaunch()
    {
        Map params = new HashMap<>();

        DSCI.getInstance().getUXInstance().navigateTo(AppView.APP_LAUNCH.name(), AppView.APP_LAUNCH.name(), params, true);
    }

    public static void showAppHome()
    {
        Map params = new HashMap<>();

        DSCI.getInstance().getUXInstance().navigateTo(AppView.APP_HOME.name(), AppView.APP_HOME.name(), params, true);
    }

    public static void showAppSettings(boolean fromLaunch)
    {
        Map params = new HashMap<>();
        params.put("FROM_LAUNCH", fromLaunch);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.APP_SETTINGS.name(), AppView.APP_SETTINGS.name(), params, true);
    }

    public static void showAppLogin()
    {
        Map params = new HashMap<>();

        DSCI.getInstance().getUXInstance().navigateTo(AppView.APP_LOGIN.name(), AppView.APP_LOGIN.name(), params, true);
    }

    public static void showAppUserSettings()
    {
        Map params = new HashMap<>();

        DSCI.getInstance().getUXInstance().navigateTo(AppView.APP_USER_SETTINGS.name(), AppView.APP_USER_SETTINGS.name(), params, true);
    }

    public static void showAssessmentCreate(String accountKey, String projectUID, String platformUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);
        params.put("PLATFORM_UID", platformUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.ASSESSMENT_CREATE.name(), AppView.ASSESSMENT_CREATE.name(), params, true);
    }

    public static void showAssessmentViewer(String accountKey, String projectUID, String platformUID, String assessmentUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);
        params.put("PLATFORM_UID", platformUID);
        params.put("ASSESSMENT_UID", assessmentUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.ASSESSMENT_VIEWER.name(), AppView.ASSESSMENT_VIEWER.name(), params, true);
    }

    public static void showCaptureViewer(String accountKey, String projectUID, String platformUID, String assessmentUID, String captureUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);
        params.put("PLATFORM_UID", platformUID);
        params.put("ASSESSMENT_UID", assessmentUID);
        params.put("CAPTURE_UID", captureUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.CAPTURE_VIEWER.name(), AppView.CAPTURE_VIEWER.name(), params, true);
    }

    public static void showMatrixCreate(String accountKey, String projectUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.MATRIX_CREATE.name(), AppView.MATRIX_CREATE.name(), params, true);
    }

    public static void showMatrixViewer(String accountKey, String projectUID, String matrixUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);
        params.put("MATRIX_UID", matrixUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.MATRIX_VIEWER.name(), AppView.MATRIX_VIEWER.name(), params, true);
    }

    public static void showPlatformCreate(String accountKey, String projectUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.PLATFORM_CREATE.name(), AppView.PLATFORM_CREATE.name(), params, true);
    }

    public static void showPlatformViewer(String accountKey, String projectUID, String platformUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);
        params.put("PLATFORM_UID", platformUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.PLATFORM_VIEWER.name(), AppView.PLATFORM_VIEWER.name(), params, true);
    }

    public static void showProjectCreate(String accountKey)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.PROJECT_CREATE.name(), AppView.PROJECT_CREATE.name(), params, true);
    }

    public static void showProjectViewer(String accountKey, String projectUID)
    {
        Map params = new HashMap<>();
        params.put("ACCOUNT_KEY", accountKey);
        params.put("PROJECT_UID", projectUID);

        DSCI.getInstance().getUXInstance().navigateTo(AppView.PROJECT_VIEWER.name(), AppView.PROJECT_VIEWER.name(), params, true);
    }
}

