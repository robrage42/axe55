package com.dsc.insights.ux.account.project.matrix;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.core.matrix.MatrixInstance;
import com.dsc.insights.core.matrix.MatrixSettings;
import com.dsc.insights.core.platform.PlatformInstance;
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
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatrixViewer implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField textKey;
    @FXML
    private TextField textName;

    private final String APP_VIEW = "MatrixViewer";

    private UXInstance uxInstance;
    private AppUXController appController;

    private String accountKey;
    private String projectUID;
    private String platformUID;
    private String matrixUID;

    private AccountInstance accountInstance;
    private AccountSettings accountSettings;
    private ProjectInstance projectInstance;
    private PlatformInstance platformInstance;
    private MatrixInstance matrixInstance;

    @FXML
    private void initialize()
    {
        ImageViewEditor.getInstance().register(ivLogo, new IImageViewListener()
        {
            @Override
            public void onImageLoaded(BufferedImage bi, String sourceURL)
            {
                boolean saved = matrixInstance.saveLogo(bi);
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
        matrixUID = (String)params.get("MATRIX_UID");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        accountSettings = accountInstance.getSettings();
        projectInstance = accountInstance.getProject(projectUID);
        matrixInstance = projectInstance.getMatrix(matrixUID);

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
        labels.add("Matrix");
        labels.add(matrixInstance.getSettings().getName());

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

    private void loadUX()
    {
        MatrixSettings settings = matrixInstance.getSettings();

        Image imageLogo = matrixInstance.getLogo();
        if (imageLogo != null)
        {
            ivLogo.setImage(imageLogo);
        }
        else
        {
            ivLogo.setImage(AppImages.MATRIX.getImage());
        }

        textKey.setText(settings.getKey());
        textName.setText(settings.getName());
    }

}
