package com.dsc.insights;

import com.dsc.insights.core.account.AccountManager;
import com.dsc.insights.ux.AppNavigator;
import com.pxg.commons.TUIDCommon;
import com.pxg.jfx.mwa.MWAService;
import com.pxg.jfx.mwa.UXInstance;
import com.pxg.media.image.GradedImageScaler;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;

public class DSCI
{
    private static DSCI instance;
    private static ExecutorService uxThreadPool;

    private Application app;
    private Stage mainStage;
    private Preferences prefStore;
    private HostServices hostServices;
    private ResourceBundle resources;
    private UXInstance uxInstance;
    private AppController appController;
    private GradedImageScaler gradedScaler;

    private transient boolean started;
    private transient boolean prepared;

    private String settingsDirectory;

    private AccountManager accountManager;

    private DSCI()
    {

    }

    public void configure(Application inApp, Stage inStage)
    {
        app = inApp;
        mainStage = inStage;

        started = false;
        prepared = false;

        prefStore = Preferences.userRoot().node("com/dsc/insights");

        hostServices = app.getHostServices();

        Locale locale = new Locale.Builder().setLanguage("EN").build();
        resources = ResourceBundle.getBundle("com/dsc/insights/lang/desktop", locale);

        appController = new AppController();
        MWAService.getService().setController(appController);

        uxInstance = MWAService.getService().open("DSCI", "INSIGHTS");
        uxInstance.show();

        uxInstance.loadBackground(AppImages.BACKGROUND.getImage());

        TUIDCommon.getInstance().configureTUID(true, true);

        AppNavigator.showAppLaunch();
    }

    public static DSCI getInstance()
    {
        if (instance == null)
        {
            instance = new DSCI();
        }

        return instance;
    }

    public void exit()
    {

        System.exit(0);
    }

    public String getPreference(String key, String defValue)
    {
        return prefStore.get(key, defValue);
    }

    public void savePreference(String key, String value)
    {
        prefStore.put(key, value);
    }

    public UXInstance getUXInstance()
    {
        return uxInstance;
    }

    public HostServices getHostServices()
    {
        return hostServices;
    }

    public ResourceBundle getResources()
    {
        return resources;
    }

    public AccountManager getAccountMananger()
    {
        return accountManager;
    }

    public boolean loadSettings()
    {
        settingsDirectory = prefStore.get("settings_dir", "");

        if (settingsDirectory.isEmpty())
        {
            return false;
        }

        if (accountManager == null)
        {
            accountManager = new AccountManager();
            accountManager.reload();
        }
        accountManager.reload();

        return true;
    }

    public String getSettingsDirectory()
    {
        return settingsDirectory;
    }

    public GradedImageScaler getGradedScaler()
    {
        if (gradedScaler == null)
        {
            gradedScaler = new GradedImageScaler();
        }
        return gradedScaler;
    }

    public static ExecutorService getUXThreadPool()
    {
        if (uxThreadPool == null)
        {
            uxThreadPool = Executors.newCachedThreadPool();
        }
        return uxThreadPool;
    }

}
