package com.dsc.insights.ux.account.project;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.project.ProjectInstance;
import com.dsc.insights.core.project.ProjectSettings;
import com.dsc.insights.ux.AppNavigator;
import com.dsc.insights.ux.AppUXController;
import com.pxg.commons.CommonValidator;
import com.pxg.commons.UIDCommon;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.controls.IImageViewListener;
import com.pxg.jfx.controls.ImageViewEditor;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.MessageLevel;
import com.pxg.jfx.mwa.UXInstance;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectCreate implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField textKey;
    @FXML
    private TextField textName;

    private final String APP_VIEW = "ProjectCreate";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;

    private AccountInstance accountInstance;
    private AccountSettings accountSettings;

    private BufferedImage biLogo;

    @FXML
    private void initialize()
    {
        ImageViewEditor.getInstance().register(ivLogo, new IImageViewListener()
        {
            @Override
            public void onImageLoaded(BufferedImage bi, String sourceURL)
            {
                biLogo = bi;
            }
        });
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

        accountKey = (String)params.get("ACCOUNT_KEY");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        accountSettings = accountInstance.getSettings();

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
    private void onCreate()
    {
        String key = textKey.getText();
        String name = textName.getText();

        if (CommonValidator.anyNullBlank(key, name) == true)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Project key and name must be provided", 5000));
            return;
        }

        String projectUID = UIDCommon.getInstance().create();

        ProjectSettings projectSettings = new ProjectSettings();
        projectSettings.setUID(projectUID);
        projectSettings.setKey(key);
        projectSettings.setName(name);

        ProjectInstance projectInstance = accountInstance.createProject(projectSettings);
        if (projectInstance == null)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Unable to create project", 5000));
            return;
        }

        if (biLogo != null)
        {
            boolean savedLogo = projectInstance.saveLogo(biLogo);
            if (savedLogo == false)
            {
                uxInstance.showMessage(new Message(MessageLevel.WARN, APP_VIEW, "Unable to save project logo", 5000));
            }
        }

        uxInstance.showMessage(new Message(MessageLevel.STATUS, APP_VIEW, "Created Project: " + name, 5000));

        AppNavigator.showProjectViewer(accountKey, projectUID);
    }

    @FXML
    private void onCancel()
    {
        AppNavigator.showAccountViewer(accountKey);
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
        labels.add(accountSettings.getName());
        labels.add("Projects");
        labels.add("Create");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (pos == 1)
                {
                    AppNavigator.showAppHome();
                }
                else
                {
                    AppNavigator.showAccountViewer(accountKey);
                }
            }
        });
    }

    private void load()
    {
        //Reset the logo
        biLogo = null;
        ivLogo.setImage(AppImages.PROJECT.getImage());

        textKey.setText("");
        textName.setText("");
    }
}
