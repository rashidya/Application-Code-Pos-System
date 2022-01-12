package lk.ijse.pos_system.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class UserFormController {
    public AnchorPane userFormContext;
    public Label lblTime;
    public Label lblDate;



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

    public void openPlaceOrderForm(ActionEvent actionEvent) throws IOException {
        openPlaceOrder();
    }

    public void openManageOrdersForm(ActionEvent actionEvent) throws IOException {
        openManageOrders();
    }

    public void openPlaceOrderFormOnMouseClick(MouseEvent mouseEvent) throws IOException {
        openPlaceOrder();
    }

    private void openPlaceOrder() throws IOException {
        navigate("../view/PlaceOrderForm.fxml",25,300);

    }

    public void openManageOrdersFormOnMouseClick(MouseEvent mouseEvent) throws IOException {
        openManageOrders();
    }

    private void openManageOrders() throws IOException {
        navigate("../view/ManageOrderForm.fxml",75,400);

    }

    private void navigate(String path, double X, double Y) throws IOException {

        Stage window = (Stage)userFormContext.getScene().getWindow();
        window.setY(X);
        window.setX(Y);
        window.setScene(new Scene(FXMLLoader.load(getClass().getResource(path))));
    }




}
