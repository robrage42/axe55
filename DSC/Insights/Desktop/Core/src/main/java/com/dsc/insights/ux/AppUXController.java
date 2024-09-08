package com.dsc.insights.ux;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.pxg.jfx.FXCallback;
import com.pxg.jfx.IFXCallback;
import com.pxg.jfx.StageManager;
import com.pxg.jfx.efx.EFXPane;
import com.pxg.jfx.efx.effects.IEFXEffect;
import com.pxg.jfx.fxml.FXMLInstance;
import com.pxg.jfx.mwa.IUXController;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.UXInstance;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

public class AppUXController implements IUXController
{
    public static final String UX_CONTROLLER_KEY = "APP_UX_CONTROLLER";

    private final String type;
    private final String key;

    public static final int MIN_WIDTH = 1000;
    public static final int MIN_HEIGHT = 700;

    private UXInstance instance;
    private Stage stage;

    private long hideTime = 500;
    private long showTime = 500;

    private StageManager stageManager;

    private EFXPane efxPane;

    private AppHeader appHeader;

    public AppUXController(String inType, String inKey)
    {
        type = inType;
        key = inKey;
    }

    @Override
    public void configure(Stage inStage)
    {
        stage = inStage;

        Image imageIcon = AppImages.LOGO.getImage();

        stage.setTitle("DSC: Insights");
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.getIcons().add(imageIcon);
        stage.setResizable(true);
        stage.setWidth(MIN_WIDTH);
        stage.setHeight(MIN_HEIGHT);
    }

    @Override
    public void onCreate(UXInstance inInstance)
    {
        instance = inInstance;
        instance.getAppStack().getStylesheets().add("com/dsc/insights/styles.css");

        stageManager = new StageManager(stage);
        stageManager.configureResize(MIN_WIDTH, MIN_HEIGHT, 99999, 99999);

        //Register itself in the cache
        instance.addCache(UX_CONTROLLER_KEY, this);

        //Install the EFXPane in the View layer
        Color bgColor = Color.valueOf("#000a14");
        efxPane = new EFXPane(25, bgColor);

        instance.setBackground(efxPane.getPane());

        //Repaint the EFXPane on a resize
        instance.getAppStack().widthProperty().addListener(changeEvent -> { resizeEFXPane();});
        instance.getAppStack().heightProperty().addListener(changeEvent -> { resizeEFXPane();});

        FXMLInstance fiHeader = new FXMLInstance("/com/dsc/insights/ux/AppHeader.fxml", DSCI.getInstance().getResources());
        fiHeader.create();
        appHeader = (AppHeader)fiHeader.getController();
        instance.setContentTop(fiHeader.getComponent());

        stageManager.addMoveListeners(appHeader.getBoxMain());
    }

    @Override
    public void onClose()
    {

    }

    @Override
    public void onViewChange(String viewType, String viewKey)
    {

    }

    @Override
    public String getFXMLPath(String viewName)
    {
        AppView view = AppView.valueOf(viewName.toUpperCase());
        if (view != null)
        {
            return view.getFXMLPath();
        }
        return null;
    }

    @Override
    public ResourceBundle getResources()
    {
        return DSCI.getInstance().getResources();
    }

    @Override
    public long getHideTime()
    {
        return hideTime;
    }

    @Override
    public long getShowTime()
    {
        return showTime;
    }

    @Override
    public void showMessage(Message message)
    {
        appHeader.showMessage(message);
    }

    public StageManager getStageManager()
    {
        return stageManager;
    }

    public void addEffect(IEFXEffect effect)
    {
        efxPane.addEffect(effect);
    }

    public void setBackgroundImagePath(String imagePath)
    {
        efxPane.setBackground(imagePath);
    }

    public void setBackgroundImage(BufferedImage bi)
    {
        efxPane.setBackground(bi);
    }

    public void setBackgroundColor(Color color)
    {
        efxPane.setBackgroundColor(color);
    }

    public AppHeader getAppHeader()
    {
        return appHeader;
    }

    private boolean resizePending = false;
    private void resizeEFXPane()
    {
        //Ignore any resize calls while request pending
        if (resizePending == false)
        {
            resizePending = true;
            FXCallback.getInstance().execute("EFXPANE_Resize", 50, new IFXCallback()
            {
                @Override
                public void onCallback(String key, long duration)
                {
                    resizePending = false;
                    efxPane.setSize(instance.getAppStack().getWidth(), instance.getAppStack().getHeight());
                }
            });
        }
    }

}
