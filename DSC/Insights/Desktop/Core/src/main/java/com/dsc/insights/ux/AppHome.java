package com.dsc.insights.ux;

import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountSettings;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.UXInstance;
import com.pxg.jfx.sir.SIRFactory;
import com.pxg.jfx.sir.SIRImageVLabel;
import com.pxg.jfx.sir.SIRListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppHome implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private FlowPane flowAccounts;

    private final String APP_VIEW = "AppHome";

    private UXInstance uxInstance;
    private AppUXController appController;

    @FXML
    private void initialize()
    {
    }

    @Override
    public void createUX(UXInstance instance, Map params)
    {
        uxInstance = instance;
        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        loadBreadcrumbs();

        loadAccounts();

        layoutUX(showTime, false);
    }

    @Override
    public void hideUX(long hideTime)
    {
        flyoutUX(hideTime);
    }

    @FXML
    private void onCreate()
    {
        AppNavigator.showAccountCreate();
    }

    @FXML
    private void onSettings()
    {
        AppNavigator.showAppSettings(false);
    }

    @FXML
    private void onUserSettings()
    {
        AppNavigator.showAppUserSettings();
    }

    @FXML
    private void onOpen()
    {

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

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {

            }
        });
    }

    private void loadAccounts()
    {
        flowAccounts.getChildren().clear();

        for (AccountSettings nextAccount: DSCI.getInstance().getAccountMananger().getAccounts().values())
        {

//            AccountInstance nextInstance = DSCI.getInstance().getAccountMananger().getAccount(nextAccount.getKey());

            Image imageLogo = DSCI.getInstance().getAccountMananger().getAccountLogo(nextAccount.getKey());

            List<String> listLabels = new ArrayList<>();
            listLabels.add(nextAccount.getKey());
            listLabels.add(nextAccount.getName());

            SIRImageVLabel<AccountSettings> ir = SIRFactory.createImageVLabel(nextAccount, imageLogo, 64, listLabels, new SIRListener<AccountSettings>()
            {
                @Override
                public void onSelect(AccountSettings entry)
                {
                    AppNavigator.showAccountViewer(entry.getKey());
                }
            });

            ir.getMainPane().setPrefWidth(300);
            ir.getMainPane().getStyleClass().add("card-status");
            ir.getLabel(0).getStyleClass().add("card-status-title");
            ir.getMainPane().getStyleClass().add("clickable");
            ir.getMainPane().setPadding(new Insets(10, 10, 10, 10));

            flowAccounts.getChildren().add(ir.getMainPane());
        }
    }
}
