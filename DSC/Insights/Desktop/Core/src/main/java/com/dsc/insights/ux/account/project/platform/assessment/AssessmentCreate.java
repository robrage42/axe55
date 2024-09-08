package com.dsc.insights.ux.account.project.platform.assessment;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.assessment.AssessmentInstance;
import com.dsc.insights.core.assessment.AssessmentSettings;
import com.dsc.insights.core.platform.PlatformInstance;
import com.dsc.insights.core.project.ProjectInstance;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AssessmentCreate implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField textKey;
    @FXML
    private TextField textName;

    private final String APP_VIEW = "AssessmentCreate";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;
    private String projectUID;
    private String platformUID;

    private AccountInstance accountInstance;
    private AccountSettings accountSettings;
    private ProjectInstance projectInstance;
    private PlatformInstance platformInstance;

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
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        appController = (AppUXController)uxInstance.getCache(AppUXController.UX_CONTROLLER_KEY);

        accountKey = (String)params.get("ACCOUNT_KEY");
        projectUID = (String)params.get("PROJECT_UID");
        platformUID = (String)params.get("PLATFORM_UID");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        accountSettings = accountInstance.getSettings();
        projectInstance = accountInstance.getProject(projectUID);
        platformInstance = projectInstance.getPlatform(platformUID);

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
    private void onCancel()
    {
        AppNavigator.showPlatformViewer(accountKey, projectUID, platformUID);
    }

    @FXML
    private void onCreate()
    {
        String key = textKey.getText();
        String name = textName.getText();

        if (CommonValidator.anyNullBlank(key, name) == true)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Assessment key and name must be provided", 5000));
            return;
        }

        String assessmentUID = UIDCommon.getInstance().create();

        AssessmentSettings assessmentSettings = new AssessmentSettings();
        assessmentSettings.setUID(assessmentUID);
        assessmentSettings.setKey(key);
        assessmentSettings.setName(name);

        boolean created = platformInstance.createAssessment(assessmentSettings);
        if (created == false)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Error creating assessment", 5000));
            return;
        }

        if (biLogo != null)
        {
            AssessmentInstance instance = platformInstance.getAssessment(assessmentUID);

            boolean savedLogo = instance.saveLogo(biLogo);
            if (savedLogo == false)
            {
                uxInstance.showMessage(new Message(MessageLevel.WARN, APP_VIEW, "Unable to save assessment logo", 5000));
            }
        }

        AppNavigator.showAssessmentViewer(accountKey, projectUID, platformUID, assessmentUID);
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
                if (pos == 2 || pos == 3)
                {
                    AppNavigator.showAccountViewer(accountKey);
                }
                else if (pos == 4 || pos == 5)
                {
                    AppNavigator.showProjectViewer(accountKey, projectUID);
                }
                else
                {
                    AppNavigator.showPlatformViewer(accountKey, projectUID, platformUID);
                }
            }
        });
    }

    private void load()
    {
        //Reset the logo
        biLogo = null;
        ivLogo.setImage(AppImages.PLATFORM.getImage());

        textKey.setText("");
        textName.setText("");
    }
}
