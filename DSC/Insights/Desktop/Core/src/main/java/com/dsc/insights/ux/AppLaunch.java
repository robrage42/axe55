package com.dsc.insights.ux;

import com.dsc.insights.DSCI;
import com.dsc.insights.ux.cards.StatusCard;
import com.pxg.commons.Callback;
import com.pxg.commons.ICallback;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.UXInstance;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AppLaunch implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private VBox boxStatusCards;

    private final String APP_VIEW = "AppLaunch";

    private UXInstance uxInstance;
    private AppUXController appController;
    private Map<String, StatusCard> statusCards;

    public AppLaunch()
    {

    }

    @FXML
    private void initialize()
    {

    }

    @Override
    public void createUX(UXInstance instance, Map params)
    {
        uxInstance = instance;

        buildStatusCards();
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);

        loadBreadcrumbs();

        resetStatusCards();

        layoutUX(showTime, false);

        //Call the onAppStart after the animation time is complete
        Callback.getInstance().execute("START_APP", showTime, new ICallback()
        {
            @Override
            public void onCallback(String key, long duration)
            {
                onAppStart();
            }
        });
    }

    @Override
    public void hideUX(long hideTime)
    {

    }

    private void layoutUX(long showTime, boolean onResize)
    {
        appController.getAppHeader().onStart(showTime);
    }

    private void loadBreadcrumbs()
    {
        List<String> labels = new ArrayList<>();
        labels.add("Launch");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {

            }
        });
    }

    private void buildStatusCards()
    {
        statusCards = new LinkedHashMap<>();

        statusCards.put("SETTINGS", new StatusCard("SETTINGS", "Load Settings"));

        boxStatusCards.getChildren().clear();
        for (StatusCard nextCard: statusCards.values())
        {
            boxStatusCards.getChildren().add(nextCard.getBoxMain());
        }
    }

    private void resetStatusCards()
    {
        for (StatusCard nextCard: statusCards.values())
        {
            nextCard.setStatus(StatusCard.STATUS_PENDING);
        }
    }

    private void onAppStart()
    {
        //Load App Settings
        statusCards.get("SETTINGS").setStatus(StatusCard.STATUS_RUNNING);
        boolean loadedSettings = DSCI.getInstance().loadSettings();
        if (loadedSettings == false)
        {
            statusCards.get("SETTINGS").setStatus(StatusCard.STATUS_ERROR);
            AppNavigator.showAppSettings(true);
            return;
        }
        statusCards.get("SETTINGS").setStatus(StatusCard.STATUS_COMPLETE);

        AppNavigator.showAppLogin();
    }
}
