package lk.ijse.pos_system.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lk.ijse.pos_system.business.BOFactory;
import lk.ijse.pos_system.business.SuperBO;
import lk.ijse.pos_system.business.custom.Impl.ManageItemsBOImpl;
import lk.ijse.pos_system.business.custom.Impl.ManageOrderBOImpl;
import lk.ijse.pos_system.business.custom.Impl.PurchaseOrderBOImpl;
import lk.ijse.pos_system.business.custom.Impl.SystemReportsBOImpl;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Formatter;

public class AdminFormController {
    public Label lblDate;
    public Label lblTime;
    public AnchorPane adminFormContext;


    public void initialize(){
        setLocalDateAndTime();
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        navigate("../view/LoginForm.fxml",350,750);

    }

    private void setLocalDateAndTime() {
        // load Date
        Date date = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        lblDate.setText(f.format(date));

        // load Time
        {
            Thread clock = new Thread() {
                public void run() {
                    try {
                        while (true) {
                            Platform.runLater(() -> {
                                lblTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
                            });
                            sleep(1);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            clock.start();

        }
    }

    public void openManageItemsForm() throws IOException {
        navigate("../view/ManageItemsForm.fxml",150,500);

    }

    public void openManageItemsFormOnAction(ActionEvent actionEvent) throws IOException {
        openManageItemsForm();
    }

    public void openManageItemsFormOnMouseClick(MouseEvent mouseEvent) throws IOException {
        openManageItemsForm();
    }

    public void openSystemReportsForm() throws IOException {

        navigate("../view/SystemReportsForm.fxml",150,500);

    }

    public void openSystemReportsFormOnAction(ActionEvent actionEvent) throws IOException {
        openSystemReportsForm();
    }

    public void openSystemReportsFormOnMouseClick(MouseEvent mouseEvent) throws IOException {
        openSystemReportsForm();
    }




    public void playMouseEnterAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();

            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1.2);
            scaleT.setToY(1.2);
            scaleT.play();


        }
    }

    public void playMouseExitAnimation(MouseEvent event) {
        if (event.getSource() instanceof ImageView) {
            ImageView icon = (ImageView) event.getSource();
            ScaleTransition scaleT = new ScaleTransition(Duration.millis(200), icon);
            scaleT.setToX(1);
            scaleT.setToY(1);
            scaleT.play();

            icon.setEffect(null);

        }
    }

    private void navigate(String path, double X, double Y) throws IOException {

        Stage window = (Stage)adminFormContext.getScene().getWindow();
        window.setY(X);
        window.setX(Y);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource(path))));
    }





}
