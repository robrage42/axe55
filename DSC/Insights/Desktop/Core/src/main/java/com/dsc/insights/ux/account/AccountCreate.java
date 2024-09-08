package com.dsc.insights.ux.account;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
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
import com.pxg.jfx.mwa.validator.TextValidator;
import com.pxg.jfx.mwa.validator.ValidatorFactory;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountCreate implements IUXView
{
    @FXML
    private VBox boxMain;
    @FXML
    private ImageView ivLogo;
    @FXML
    private TextField textKey;
    @FXML
    private TextField textName;

    private final String APP_VIEW = "AccountCreate";

    private UXInstance uxInstance;
    private AppUXController appController;

    private BufferedImage biLogo;

    private TextValidator validateKey;
    private TextValidator validateNamespace;
    private TextValidator validateName;
    private TextValidator validateDevKey;

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

        validateKey = ValidatorFactory.createText(uxInstance, APP_VIEW, "Key");
        validateName = ValidatorFactory.createText(uxInstance, APP_VIEW, "Name");
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        clear();

        loadBreadcrumbs();

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
        if (validate() == false)
        {
            return;
        }

        String accountKey = textKey.getText();
        String accountName = textName.getText();

        if (CommonValidator.anyNullBlank(accountKey, accountName) == true)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "All fields are required", 5000));
            return;
        }

        String accountUID = UIDCommon.getInstance().create();

        AccountSettings account = new AccountSettings();
        account.setUID(accountUID);
        account.setKey(accountKey);
        account.setName(accountName);

        boolean saved = DSCI.getInstance().getAccountMananger().createAccount(account);
        if (saved == false)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Error creating account", 5000));
            return;
        }

        if (biLogo != null)
        {
            AccountInstance instance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
            boolean savedLogo = instance.saveLogo(biLogo);
            if (savedLogo == false)
            {
                uxInstance.showMessage(new Message(MessageLevel.WARN, APP_VIEW, "Unable to save custom logo", 5000));
            }
        }

        uxInstance.showMessage(new Message(MessageLevel.STATUS, APP_VIEW, "Created account: " + accountName, 5000));

        AppNavigator.showAccountViewer(accountKey);
    }

    @FXML
    private void onCancel()
    {
        AppNavigator.showAppHome();
    }

    private boolean validate()
    {
        if (validateKey.validate(textKey.getText()) == false)
        {
            return false;
        }
        if (validateName.validate(textName.getText()) == false)
        {
            return false;
        }
        return true;
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
        labels.add("Accounts");
        labels.add("Create");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                AppNavigator.showAppHome();
            }
        });
    }

    private void clear()
    {
        //Reset the logo
        biLogo = null;
        ivLogo.setImage(AppImages.ACCOUNT.getImage());

        textKey.setText("");
        textName.setText("");
    }
}
