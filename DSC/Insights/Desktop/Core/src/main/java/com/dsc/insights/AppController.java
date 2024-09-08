package com.dsc.insights;

import com.dsc.insights.ux.AppUXController;
import com.pxg.jfx.mwa.IMWAController;
import com.pxg.jfx.mwa.IUXController;

public class AppController implements IMWAController
{
    public AppController()
    {

    }

    @Override
    public IUXController create(String type, String key)
    {
        AppUXController uxController = new AppUXController(type, key);

        return uxController;
    }

    @Override
    public String getStyleSheet()
    {
        return "com/dsc/insights/styles.css";
    }

    @Override
    public String getStyleClass()
    {
        return "app-stack";
    }

    @Override
    public void onClose(String type, String key)
    {

    }

    @Override
    public void onExit()
    {
        DSCI.getInstance().exit();
    }
}
