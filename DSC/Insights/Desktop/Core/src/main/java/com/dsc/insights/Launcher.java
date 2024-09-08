package com.dsc.insights;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        DSCI.getInstance().configure(this, stage);
    }
}
