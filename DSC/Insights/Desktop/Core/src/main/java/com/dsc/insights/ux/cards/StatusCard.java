package com.dsc.insights.ux.cards;

import com.dsc.insights.AppImages;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class StatusCard
{
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_RUNNING = 20;
    public static final int STATUS_COMPLETE = 50;
    public static final int STATUS_ERROR = 90;

    private final String key;
    private final String title;

    private int status;

    private HBox boxMain;
    private Label labelTitle;
    private Label labelStatus;
    private VBox boxIcon;
    private ImageView ivIcon;

    public StatusCard(String inKey, String inTitle)
    {
        key = inKey;
        title = inTitle;

        buildUX();

        setStatus(STATUS_PENDING);
    }

    public HBox getBoxMain()
    {
        return boxMain;
    }

    public void setStatusMessage(String message)
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                labelStatus.setText(message);
            }
        });
    }

    public void setStatus(int changedStatus)
    {
        //Check for a change in status
        if (status == changedStatus)
        {
            return;
        }

        status = changedStatus;

        updateUX();
    }

    private void updateUX()
    {
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                switch (status)
                {
                    case STATUS_PENDING:
                        labelStatus.setText("Pending");
                        boxIcon.setBackground(Background.fill(Color.DARKORCHID));
                        ivIcon.setImage(AppImages.STATUS_PENDING.getImage());
                        break;
                    case STATUS_RUNNING:
                        labelStatus.setText("Running");
                        boxIcon.setBackground(Background.fill(Color.DARKORANGE));
                        ivIcon.setImage(AppImages.STATUS_RUNNING.getImage());
                        break;
                    case STATUS_COMPLETE:
                        labelStatus.setText("Complete");
                        boxIcon.setBackground(Background.fill(Color.DARKGREEN));
                        ivIcon.setImage(AppImages.STATUS_COMPLETE.getImage());
                        break;
                    case STATUS_ERROR:
                        labelStatus.setText("Error");
                        boxIcon.setBackground(Background.fill(Color.DARKRED));
                        ivIcon.setImage(AppImages.STATUS_ERROR.getImage());
                        break;
                }
            }
        });
    }

    private void buildUX()
    {
        boxMain = new HBox();
        boxMain.setAlignment(Pos.CENTER_LEFT);
        boxMain.setPadding(new Insets(5,5,5,5));
        boxMain.setSpacing(5);
        boxMain.getStyleClass().add("card-status");

        labelTitle = new Label(title);
        boxMain.getChildren().add(labelTitle);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        boxMain.getChildren().add(region);

        labelStatus = new Label("Pending");
        boxMain.getChildren().add(labelStatus);

        boxIcon = new VBox();
        boxIcon.setAlignment(Pos.CENTER);
        boxIcon.setPadding(new Insets(5,5,5,5));
        boxMain.getChildren().add(boxIcon);

        ivIcon = new ImageView(AppImages.STATUS_PENDING.getImage());
        ivIcon.setFitWidth(24);
        ivIcon.setFitHeight(24);
        boxIcon.getChildren().add(ivIcon);
    }
}
