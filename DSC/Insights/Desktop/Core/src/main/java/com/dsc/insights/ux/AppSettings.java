package com.dsc.insights.ux;

import com.dsc.insights.DSCI;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.form.FormBox;
import com.pxg.jfx.form.FormItemFactory;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.MessageLevel;
import com.pxg.jfx.mwa.UXInstance;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppSettings implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private VBox boxSettings;

    private final String APP_VIEW = "AppSettings";

    private UXInstance uxInstance;
    private AppUXController appController;
    private Boolean fromLaunch;
    private FormBox formBox;

    @FXML
    private void initialize()
    {

    }

    @Override
    public void createUX(UXInstance instance, Map params)
    {
        uxInstance = instance;
//        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);

        fromLaunch = (Boolean)params.get("FROM_LAUNCH");

        loadBreadcrumbs();

        load();

        layoutUX(showTime, false);
    }

    @Override
    public void hideUX(long hideTime)
    {
        flyoutUX(hideTime);
    }

    @FXML
    private void onSave()
    {
        boolean validated = formBox.validate();
        if (validated == false)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Missing or incorrect settings. Please verify", 5000));
            return;
        }

        Map<String, Object> formInputs = formBox.save();
        String settingsDirectory = (String)formInputs.get("SETTINGS_PATH");

        //Set the settings directory and store in preferences
        if (settingsDirectory != null)
        {
            DSCI.getInstance().savePreference("settings_dir", settingsDirectory);
            DSCI.getInstance().loadSettings();
        }

        AppNavigator.showAppLogin();
    }

    @FXML
    private void onCancel()
    {
        if (fromLaunch)
        {
            AppNavigator.showAppLaunch();
        }
        else
        {
            AppNavigator.showAppHome();
        }
    }

    private void layoutUX(long showTime, boolean onResize)
    {
//        Timeline timeline = new Timeline();
//        timeline.play();
    }

    private void flyoutUX(long hideTime)
    {
//        Timeline timeline = new Timeline();
//        timeline.play();
    }

    private void loadBreadcrumbs()
    {
        List<String> labels = new ArrayList<>();
        if (fromLaunch == true)
        {
            labels.add("Settings");
        }
        else
        {
            labels.add("Home");
            labels.add("Settings");
        }

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (fromLaunch == true)
                {
                    return;
                }

                if (pos == 1)
                {
                    AppNavigator.showAppHome();
                }
            }
        });
    }

    private void load()
    {
        boxSettings.getChildren().clear();

        String settingsDir = DSCI.getInstance().getSettingsDirectory();

        formBox = new FormBox();
        formBox.add(FormItemFactory.directory("SETTINGS_PATH", null, "Settings Path", settingsDir, "Path to directory for all DSC Insight settings", true));

        boxSettings.getChildren().add(formBox);
    }
}
