package com.dsc.insights.ux.account;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.project.ProjectInstance;
import com.dsc.insights.core.project.ProjectSettings;
import com.dsc.insights.ux.AppNavigator;
import com.dsc.insights.ux.AppUXController;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.controls.IImageViewListener;
import com.pxg.jfx.controls.ImageViewEditor;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.MessageLevel;
import com.pxg.jfx.mwa.UXInstance;
import com.pxg.jfx.sir.SIRFactory;
import com.pxg.jfx.sir.SIRImageVLabel;
import com.pxg.jfx.sir.SIRListener;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountViewer implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private Label labelKey;
    @FXML
    private Label labelName;
    @FXML
    private FlowPane flowProjects;

    private final String APP_VIEW = "AccountViewer";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;
    private AccountSettings account;
    private AccountInstance accountInstance;

    @FXML
    private void initialize()
    {
        ImageViewEditor.getInstance().register(ivLogo, new IImageViewListener()
        {
            @Override
            public void onImageLoaded(BufferedImage bi, String sourceURL)
            {
                boolean saved = accountInstance.saveLogo(bi);
                if (saved == false)
                {
                    uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Error updating logo", 5000));
                    return;
                }
                uxInstance.showMessage(new Message(MessageLevel.STATUS, APP_VIEW, "Logo updated", 5000));
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
        account = accountInstance.getSettings();

        loadBreadcrumbs();

        load();

        loadProjects();

        layoutUX(showTime, false);
    }

    @Override
    public void hideUX(long hideTime)
    {
        flyoutUX(hideTime);
    }

    @FXML
    private void onCreateProject()
    {
        AppNavigator.showProjectCreate(accountKey);
    }

    @FXML
    private void onProfile()
    {
        AppNavigator.showAccountProfile(accountKey);
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
        labels.add(account.getName());

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
        Image imageLogo = accountInstance.getLogo();
        if (imageLogo != null)
        {
            ivLogo.setImage(imageLogo);
        }
        else
        {
            ivLogo.setImage(AppImages.ACCOUNT.getImage());
        }

        labelKey.setText(account.getKey());
        labelName.setText(account.getName());
    }

    private void loadProjects()
    {
        flowProjects.getChildren().clear();

        for (ProjectSettings nextProject: accountInstance.getProjects().values())
        {
            ProjectInstance nextInstance = accountInstance.getProject(nextProject.getUID());

            Image imageLogo = nextInstance.getLogo();

            List<String> listLabels = new ArrayList<>();
            listLabels.add(nextProject.getKey());
            listLabels.add(nextProject.getName());
            listLabels.add(nextProject.getUID());

            SIRImageVLabel<ProjectSettings> ir = SIRFactory.createImageVLabel(nextProject, imageLogo, 64, listLabels, new SIRListener<ProjectSettings>()
            {
                @Override
                public void onSelect(ProjectSettings project)
                {
                    AppNavigator.showProjectViewer(project.getAccountKey(), project.getUID());
                }
            });

            ir.getMainPane().getStyleClass().add("card-status");
            ir.getMainPane().getStyleClass().add("clickable");
            ir.getMainPane().setPadding(new Insets(10, 10, 10, 10));

            flowProjects.getChildren().add(ir.getMainPane());
        }
    }
}
