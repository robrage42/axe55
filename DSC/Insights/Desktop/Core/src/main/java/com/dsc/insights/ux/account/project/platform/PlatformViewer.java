package com.dsc.insights.ux.account.project.platform;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.assessment.AssessmentInstance;
import com.dsc.insights.core.assessment.AssessmentSettings;
import com.dsc.insights.core.platform.PlatformInstance;
import com.dsc.insights.core.platform.PlatformSettings;
import com.dsc.insights.core.project.ProjectInstance;
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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlatformViewer implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField textKey;
    @FXML
    private TextField textName;
    @FXML
    private FlowPane flowAssessments;

    private final String APP_VIEW = "PlatformViewer";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;
    private String projectUID;
    private String platformUID;

    private AccountInstance accountInstance;
    private AccountSettings accountSettings;
    private ProjectInstance projectInstance;
    private PlatformInstance platformInstance;

    @FXML
    private void initialize()
    {
        ImageViewEditor.getInstance().register(ivLogo, new IImageViewListener()
        {
            @Override
            public void onImageLoaded(BufferedImage bi, String sourceURL)
            {
                boolean saved = platformInstance.saveLogo(bi);
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
        projectUID = (String)params.get("PROJECT_UID");
        platformUID = (String)params.get("PLATFORM_UID");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        accountSettings = accountInstance.getSettings();
        projectInstance = accountInstance.getProject(projectUID);
        platformInstance = projectInstance.getPlatform(platformUID);

        loadUX();

        loadBreadcrumbs();

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

    }

    @FXML
    private void onDelete()
    {

    }

    @FXML
    private void onCreateAssessment()
    {
        AppNavigator.showAssessmentCreate(accountKey, projectUID, platformUID);
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
        labels.add(projectInstance.getSettings().getName());
        labels.add("Platforms");
        labels.add(platformInstance.getSettings().getName());

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (pos == 1)
                {
                    AppNavigator.showAppHome();
                }
                if (pos == 2 || pos == 3)
                {
                    AppNavigator.showAccountViewer(accountKey);
                }
                else
                {
                    AppNavigator.showProjectViewer(accountKey, projectUID);
                }
            }
        });
    }

    private void loadUX()
    {
        PlatformSettings settings = platformInstance.getSettings();

        Image imageLogo = platformInstance.getLogo();
        if (imageLogo != null)
        {
            ivLogo.setImage(imageLogo);
        }
        else
        {
            ivLogo.setImage(AppImages.PLATFORM.getImage());
        }

        textKey.setText(settings.getKey());
        textName.setText(settings.getName());

        loadAssessments();
    }

    private void loadAssessments()
    {
        flowAssessments.getChildren().clear();

        for (AssessmentSettings nextAssessment: platformInstance.getAssessments().values())
        {
            AssessmentInstance nextInstance = platformInstance.getAssessment(nextAssessment.getUID());

            Image imageLogo = nextInstance.getLogo();

            List<String> listLabels = new ArrayList<>();
            listLabels.add(nextAssessment.getKey());
            listLabels.add(nextAssessment.getName());
            listLabels.add(nextAssessment.getUID());

            SIRImageVLabel<PlatformSettings> ir = SIRFactory.createImageVLabel(nextAssessment, imageLogo, 64, listLabels, new SIRListener<AssessmentSettings>()
            {
                @Override
                public void onSelect(AssessmentSettings assessment)
                {
                    AppNavigator.showAssessmentViewer(assessment.getAccountKey(), assessment.getProjectUID(), assessment.getPlatformUID(), assessment.getUID());
                }
            });

            ir.getMainPane().getStyleClass().add("card-status");
            ir.getLabel(0).getStyleClass().add("card-status-title");
            ir.getMainPane().getStyleClass().add("clickable");
            ir.getMainPane().setPadding(new Insets(10, 10, 10, 10));

            flowAssessments.getChildren().add(ir.getMainPane());
        }
    }
}
