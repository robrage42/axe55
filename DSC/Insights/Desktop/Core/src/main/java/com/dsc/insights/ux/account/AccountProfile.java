package com.dsc.insights.ux.account;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.dsc.insights.core.account.AccountInstance;
import com.dsc.insights.core.account.AccountSettings;
import com.dsc.insights.ux.AppNavigator;
import com.dsc.insights.ux.AppUXController;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.controls.IImageViewListener;
import com.pxg.jfx.controls.ImageViewEditor;
import com.pxg.jfx.form.FormBox;
import com.pxg.jfx.mwa.IUXView;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.MessageLevel;
import com.pxg.jfx.mwa.UXInstance;
import com.pxg.jfx.mwa.validator.*;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AccountProfile implements IUXView
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
    private TextField textNamespace;
    @FXML
    private TextField textDevKey;
    @FXML
    private TextField textURL;
    @FXML
    private TextField textBillingEmail;
    @FXML
    private TextField textBillingPhone;
    @FXML
    private TextField textBillingURL;
    @FXML
    private TextField textSalesEmail;
    @FXML
    private TextField textSalesPhone;
    @FXML
    private TextField textSalesURL;
    @FXML
    private TextField textSupportEmail;
    @FXML
    private TextField textSupportPhone;
    @FXML
    private TextField textSupportURL;
    @FXML
    private TextField textTechEmail;
    @FXML
    private TextField textTechPhone;
    @FXML
    private TextField textTechURL;
    @FXML
    private TextArea taSummary;

    private final String APP_VIEW = "AccountProfile";

    private UXInstance uxInstance;
    private AppUXController appController;
    private FormBox formBox;

    private String accountKey;
    private AccountInstance accountInstance;
    private AccountSettings account;

    private TextValidator validateNamespace;
    private TextValidator validateName;
    private URLValidator validateURL;
    private TextValidator validateDevKey;
    private EmailValidator validateEmail;
    private PhoneValidator validatePhone;
    private TextValidator validateSummary;

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

        validateNamespace = ValidatorFactory.createText(uxInstance, APP_VIEW, "Namespace");
        validateName = ValidatorFactory.createText(uxInstance, APP_VIEW, "Name");
        validateURL = ValidatorFactory.createURL(uxInstance, APP_VIEW, "URL");
        validateDevKey = ValidatorFactory.createText(uxInstance, APP_VIEW, "Developer's Key");
        validateEmail = ValidatorFactory.createEmail(uxInstance, APP_VIEW, "Email Contact");
        validatePhone = ValidatorFactory.createPhone(uxInstance, APP_VIEW, "Phone Contact");
        validateSummary = ValidatorFactory.createText(uxInstance, APP_VIEW, "Summary");
    }

    @Override
    public void showUX(long showTime, Map params)
    {
        uxInstance.setContent(boxMain);

        accountKey = (String)params.get("ACCOUNT_KEY");

        accountInstance = DSCI.getInstance().getAccountMananger().getAccount(accountKey);
        account = accountInstance.getSettings();

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
    private void onCancel()
    {
        AppNavigator.showAccountViewer(accountKey);
    }

    @FXML
    private void onUpdate()
    {
        if (validate() == false)
        {
            return;
        }

        String name = textName.getText();
        String namespace = textNamespace.getText();
        String devKey = textDevKey.getText();
        String pubURL = textURL.getText();
        String summary = taSummary.getText();

//        account.setName(name);
//        account.setNamespace(namespace);
//        account.setDeveloperKey(devKey);
//        account.setURL(pubURL);
//        account.setSummary(summary);
//
//        account.getBillingContact().setEmail(textBillingEmail.getText());
//        account.getBillingContact().setPhone(textBillingPhone.getText());
//        account.getBillingContact().setURL(textBillingURL.getText());
//
//        account.getSalesContact().setEmail(textSalesEmail.getText());
//        account.getSalesContact().setPhone(textSalesPhone.getText());
//        account.getSalesContact().setURL(textSalesURL.getText());
//
//        account.getTechnicalContact().setEmail(textTechEmail.getText());
//        account.getTechnicalContact().setPhone(textTechPhone.getText());
//        account.getTechnicalContact().setURL(textTechURL.getText());
//
//        account.getSupportContact().setEmail(textSupportEmail.getText());
//        account.getSupportContact().setPhone(textSupportPhone.getText());
//        account.getSupportContact().setURL(textSupportURL.getText());

        boolean saved = accountInstance.saveSettings();
        if (saved == false)
        {
            uxInstance.showMessage(new Message(MessageLevel.ERROR, APP_VIEW, "Error saving account settings", 5000));
            return;
        }

        uxInstance.showMessage(new Message(MessageLevel.STATUS, APP_VIEW, "Publisher settings updated", 5000));
    }

    private boolean validate()
    {
        if (validateNamespace.validate(textNamespace.getText()) == false)
        {
            return false;
        }
        if (validateName.validate(textName.getText()) == false)
        {
            return false;
        }
        if (validateDevKey.validate(textDevKey.getText()) == false)
        {
            return false;
        }
        if (validateURL.validate(textURL.getText()) == false)
        {
            return false;
        }
        if (validateSummary.validate(taSummary.getText()) == false)
        {
            return false;
        }
        
        if (validateContact(textBillingEmail, textBillingPhone, textBillingURL) == false)
        {
            return false;
        }

        if (validateContact(textSalesEmail, textSalesPhone, textSalesURL) == false)
        {
            return false;
        }

        if (validateContact(textTechEmail, textTechPhone, textTechURL) == false)
        {
            return false;
        }

        if (validateContact(textSupportEmail, textSupportPhone, textSupportURL) == false)
        {
            return false;
        }

        return true;
    }
    
    private boolean validateContact(TextField textEmail, TextField textPhone, TextField textURL)
    {
        if (textEmail.getText().isBlank() == false)
        {
            if (validateEmail.validate(textEmail.getText()) == false)
            {
                return false;
            }
        }
        if (textPhone.getText().isBlank() == false)
        {
            if (validatePhone.validate(textPhone.getText()) == false)
            {
                return false;
            }
        }

        if (textURL.getText().isBlank() == false)
        {
            if (validateURL.validate(textURL.getText()) == false)
            {
                return false;
            }
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
        labels.add(account.getName());
        labels.add("Profile");

        appController.getAppHeader().loadBreadcrumb(labels, new IBreadcrumbListener()
        {
            @Override
            public void onClick(int pos, String text)
            {
                if (pos == 1)
                {
                    AppNavigator.showAppHome();
                }
                else if (pos == 2)
                {
                    AppNavigator.showAccountViewer(accountKey);
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

        textKey.setText(account.getKey());
//        textName.setText(account.getName());
//        textNamespace.setText(account.getNamespace());
//        textDevKey.setText(account.getDeveloperKey());
//        textURL.setText(account.getURL());
//        taSummary.setText(account.getSummary());

//        textBillingEmail.setText(account.getBillingContact().getEmail());
//        textBillingPhone.setText(account.getBillingContact().getPhone());
//        textBillingURL.setText(account.getBillingContact().getURL());
//
//        textSalesEmail.setText(account.getSalesContact().getEmail());
//        textSalesPhone.setText(account.getSalesContact().getPhone());
//        textSalesURL.setText(account.getSalesContact().getURL());
//
//        textTechEmail.setText(account.getTechnicalContact().getEmail());
//        textTechPhone.setText(account.getTechnicalContact().getPhone());
//        textTechURL.setText(account.getTechnicalContact().getURL());
//
//        textSupportEmail.setText(account.getSupportContact().getEmail());
//        textSupportPhone.setText(account.getSupportContact().getPhone());
//        textSupportURL.setText(account.getSupportContact().getURL());
    }
}
