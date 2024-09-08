package com.dsc.insights.ux.user;

import com.dsc.insights.ux.AppNavigator;
import com.dsc.insights.ux.AppUXController;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.form.FormBox;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.UXInstance;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppUserSettings implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private VBox boxSettings;

    private final String APP_VIEW = "AppUserSettings";

    private UXInstance uxInstance;
    private AppUXController appController;
    private FormBox formBox;

    @FXML
    private void initialize()
    {

    }

    @Override
    public void createUX(UXInstance instance, Map params)
    {
        uxInstance = instance;
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);

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
        System.out.println("AppUserSettings::onSave");
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
        labels.add("Home");
        labels.add("User Settings");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (pos == 1)
                {
                    AppNavigator.showAppHome();
                }
            }
        });
    }

    private void load()
    {

    }
}
