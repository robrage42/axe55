package com.dsc.insights.ux;

import com.dsc.insights.AppImages;
import com.dsc.insights.DSCI;
import com.pxg.jfx.FXCallback;
import com.pxg.jfx.IFXCallback;
import com.pxg.jfx.animation.Animator;
import com.pxg.jfx.animation.FrameMaker;
import com.pxg.jfx.controls.BreadcrumbFactory;
import com.pxg.jfx.controls.IBreadcrumbListener;
import com.pxg.jfx.mwa.Message;
import com.pxg.jfx.mwa.UXInstance;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.*;

public class AppHeader
{
    @FXML
    private VBox boxMain;
    @FXML
    private HBox boxBreadcrumb;
    @FXML
    private ImageView imageLogo;
    @FXML
    private Label labelMessage;

    private Message currentMessage;
    private String currentMessageKey;

    @FXML
    public void initialize()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 16);
        cal.set(Calendar.MINUTE, 20);
        cal.set(Calendar.SECOND, 0);

        if (cal.getTimeInMillis() > System.currentTimeMillis())
        {
            Timer timer = new Timer();
            timer.schedule(new TimerTask()
            {
                @Override
                public void run()
                {
                    imageLogo.setOpacity(0.0);
                    imageLogo.setImage(AppImages.PUFF.getImage());
                    Animator.getInstance().fadeIn(imageLogo, 1000);

                    FXCallback.getInstance().execute("LOGO", 60000, new IFXCallback()
                    {
                        @Override
                        public void onCallback(String key, long duration)
                        {
                            imageLogo.setOpacity(0.0);
                            imageLogo.setImage(AppImages.LOGO.getImage());
                            Animator.getInstance().fadeIn(imageLogo, 1000);
                        }
                    });
                }
            }, cal.getTime());
        }
    }

    public VBox getBoxMain()
    {
        return boxMain;
    }

    public void onStart(long showTime)
    {
        Timeline timeline = new Timeline();

        imageLogo.setScaleX(0.0);
        imageLogo.setScaleY(0.0);

        FrameMaker.scale(timeline, imageLogo, showTime, 0.0, 1.0);

        timeline.play();
    }

    public void showMessage(Message message)
    {
        //Check if there is a previous message still waiting
        if (currentMessage != null)
        {
            //Execute the auto action if needed
            if (currentMessage.getAutoAction() != null)
            {
                currentMessage.getAutoAction().onAction();
            }
        }

        //Set the new current message
        currentMessage = message;

        if (currentMessage == null || currentMessage.getContent().isBlank())
        {
            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    labelMessage.setText("");
                    labelMessage.setVisible(false);
                }
            });
            return;
        }

        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                String style = "message-" + currentMessage.getLevel().name().toLowerCase();

                labelMessage.getStyleClass().clear();
                labelMessage.getStyleClass().add("message-label");
                labelMessage.getStyleClass().add(style);

                labelMessage.setVisible(true);
                labelMessage.setText(currentMessage.getContent());
            }
        });

        if (currentMessage.getDisplayTime() > 0)
        {
            currentMessageKey = "FOOTER::MESSAGE::" + currentMessage.getSentAt();

            long delayTime = currentMessage.getDisplayTime();
            FXCallback.getInstance().execute("FOOTER::MESSAGE::" + currentMessage.getSentAt(), delayTime, new IFXCallback()
            {
                @Override
                public void onCallback(String key, long duration)
                {
                    if (key.equals(currentMessageKey))
                    {
                        labelMessage.setText("");
                        labelMessage.setVisible(false);

                        //Execute the auto action if needed
                        if (currentMessage.getAutoAction() != null)
                        {
                            currentMessage.getAutoAction().onAction();
                        }

                        currentMessage = null;
                    }
                }
            });
        }
    }

    public void loadBreadcrumb(List<String> breadcrumbs, IBreadcrumbListener listener)
    {
        BreadcrumbFactory.getInstance().load(boxBreadcrumb, breadcrumbs, new ArrayList<>(), listener);
    }

    @FXML
    private void onMinimize()
    {
        UXInstance uxInstance = DSCI.getInstance().getUXInstance();
        uxInstance.getStage().setIconified(true);
    }

    @FXML
    private void onMaximize()
    {
        UXInstance uxInstance = DSCI.getInstance().getUXInstance();
        boolean maxState = !uxInstance.getStage().isMaximized();
        uxInstance.getStage().setMaximized(maxState);
    }

    @FXML
    private void onClose()
    {
        UXInstance uxInstance = DSCI.getInstance().getUXInstance();
        uxInstance.close();
    }
}
