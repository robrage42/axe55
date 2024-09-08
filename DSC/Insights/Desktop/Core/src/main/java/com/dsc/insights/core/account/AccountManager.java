package com.dsc.insights.core.account;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.pxg.dio.JSONCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.media.image.ImageCommon;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccountManager
{
    private Map<String, AccountSettings> accounts;
    private Map<String, AccountInstance> accountInstances;

    private String accountDir;

    public AccountManager()
    {
        initDirectories();

        reload();
    }

    public Image getAccountLogo(String accountKey)
    {
        Image imageLogo = null;

        String accountDirectory = accountDir + accountKey + "/";
        File fileLogo = new File(accountDirectory, "logo.png");
        if (fileLogo.exists() == true)
        {
            BufferedImage biLogo = ImageCommon.getInstance().bufferImage(fileLogo);
            if (biLogo != null)
            {
                imageLogo = SwingFXUtils.toFXImage(biLogo, null);
            }
        }

        if (imageLogo == null)
        {
            imageLogo = AppImages.ACCOUNT.getImage();
        }

        return imageLogo;
    }

    public Map<String, AccountSettings> getAccounts()
    {
        return accounts;
    }

    public AccountInstance getAccount(String accountKey)
    {
        if (accountInstances.containsKey(accountKey) == false)
        {
            String accountDirPath = accountDir + accountKey + "/";
            File accountDir = new File(accountDirPath);
            if (accountDir.exists() == false)
            {
                return null;
            }

            AccountInstance instance = new AccountInstance(accountDir);
            if (instance.isLoaded() == false)
            {
                return null;
            }

            accountInstances.put(accountKey, instance);
        }
        return accountInstances.get(accountKey);
    }

    public boolean createAccount(AccountSettings settings)
    {
        String accountKey = settings.getKey().toUpperCase();
        String accountPath = this.accountDir + "/" + accountKey + "/";

        File accountDirectory = new File(accountPath);
        FileCommon.getInstance().makeDirectory(accountDirectory);

        String jsonContent = JSONCommon.getInstance().serialize(settings);
        File fileSettings = new File(accountDirectory, "settings.json");
        FileCommon.getInstance().writeFile(fileSettings, jsonContent, "UTF-8");

        accounts.put(accountKey, settings);

        return true;
    }

    public void reload()
    {
        accounts = new LinkedHashMap<>();
        accountInstances = new LinkedHashMap<>();

        File fileAccountsDir = new File(accountDir);

        for (File nextAccountDir : fileAccountsDir.listFiles())
        {
            if (nextAccountDir.isDirectory() == true)
            {
                File fileAccountSettings = new File(nextAccountDir, "settings.json");
                if (fileAccountSettings.exists() == true)
                {
                    AccountSettings loadSettings = new AccountSettings(fileAccountSettings);

                    accounts.put(loadSettings.getKey(), loadSettings);
                }
            }
        }
    }

    private void initDirectories()
    {
        String settingsDir = DSCI.getInstance().getSettingsDirectory();

        accountDir = settingsDir + "/accounts/";
        FileCommon.getInstance().makeDirectory(accountDir);
    }
}

