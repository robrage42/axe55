package com.dsc.insights.ux.account.project.platform.assessment.capture;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.assessment.AssessmentInstance;
import com.dsc.insights.core.assessment.AssessmentSettings;
import com.dsc.insights.core.capture.CaptureInstance;
import com.dsc.insights.core.platform.PlatformInstance;
import com.dsc.insights.core.project.ProjectInstance;
import com.dsc.insights.ux.AppNavigator;
import com.dsc.insights.ux.AppUXController;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.UXInstance;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CaptureViewer implements IUXView
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
    private FlowPane flowDetails;

    private final String APP_VIEW = "CaptureViewer";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;
    private String projectUID;
    private String platformUID;
    private String assessmentUID;
    private String captureUID;

    private AccountInstance accountInstance;
    private AccountSettings accountSettings;
    private ProjectInstance projectInstance;
    private PlatformInstance platformInstance;
    private AssessmentInstance assessmentInstance;
    private CaptureInstance captureInstance;

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

        accountKey = (String)params.get("ACCOUNT_KEY");
        projectUID = (String)params.get("PROJECT_UID");
        platformUID = (String)params.get("PLATFORM_UID");
        assessmentUID = (String)params.get("ASSESSMENT_UID");
        captureUID = (String)params.get("CAPTURE_UID");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        accountSettings = accountInstance.getSettings();
        projectInstance = accountInstance.getProject(projectUID);
        platformInstance = projectInstance.getPlatform(platformUID);
        assessmentInstance = platformInstance.getAssessment(assessmentUID);
        captureInstance = assessmentInstance.getCapture(captureUID);

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
    private void onImport()
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
        labels.add(accountSettings.getName());
        labels.add("Projects");
        labels.add(projectInstance.getSettings().getName());
        labels.add("Platforms");
        labels.add(platformInstance.getSettings().getName());
        labels.add("Assessments");
        labels.add(assessmentInstance.getSettings().getName());
        labels.add("Captures");
        labels.add(captureInstance.getSettings().getName());

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
                else if (pos == 4 || pos == 5)
                {
                    AppNavigator.showProjectViewer(accountKey, projectUID);
                }
                else if (pos == 6 || pos == 7)
                {
                    AppNavigator.showPlatformViewer(accountKey, projectUID, platformUID);
                }
                else
                {
                    AppNavigator.showAssessmentViewer(accountKey, projectUID, platformUID, assessmentUID);
                }
            }
        });
    }

    private void loadUX()
    {
        AssessmentSettings settings = assessmentInstance.getSettings();

        Image imageLogo = assessmentInstance.getLogo();
        if (imageLogo != null)
        {
            ivLogo.setImage(imageLogo);
        }
        else
        {
            ivLogo.setImage(AppImages.PROJECT.getImage());
        }

        textKey.setText(settings.getKey());
        textName.setText(settings.getName());

        loadDetails();
    }

    private void loadDetails()
    {
        flowDetails.getChildren().clear();
    }

}
