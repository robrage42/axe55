package com.dsc.insights.ux;

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

public class AppLogin implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private VBox boxLogin;

    private final String APP_VIEW = "AppLogin";

    private UXInstance uxInstance;
    private AppUXController appController;
    private Boolean fromLaunch;
    private FormBox formBox;

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
    private void onLogin()
    {
        boolean validated = formBox.validate();
        if (validated == false)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Please provide username and password", 5000));
            return;
        }

        //TODO: Authenticate user

        AppNavigator.showAppHome();
    }

    @FXML
    private void onCancel()
    {
        AppNavigator.showAppLaunch();
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
        labels.add("Login");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (pos == 1)
                {
                    return;
                }
            }
        });
    }

    private void load()
    {
        boxLogin.getChildren().clear();

        formBox = new FormBox();
        formBox.add(FormItemFactory.textInput("USERNAME", null, "Username", "", "", true));
        formBox.add(FormItemFactory.password("PASSWORD", null, "Password", "", "", true));

        boxLogin.getChildren().add(formBox);
    }
}
