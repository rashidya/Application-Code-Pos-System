package lk.ijse.pos_system.controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;



import java.io.IOException;

public class LoginFormController {
    public PasswordField txtPassword;
    public TextField txtUserName;
    public TextField txtPasswordText;
    public AnchorPane loginFormContext;




    public void logInOnAction(ActionEvent actionEvent) throws IOException {

        String path=null;

        if(txtUserName.getText().equals("User")|txtUserName.getText().equals("Admin")){
            if (txtPassword.getText().equals("1111")){
                path=(txtUserName.getText().equals("User"))?"../view/userForm.fxml":"../view/AdminForm.fxml";
                Stage window = (Stage) loginFormContext.getScene().getWindow();
                window.setY(250);
                window.setX(600);
                window.setScene(new Scene(FXMLLoader.load(getClass().getResource(path))));
            }else{
                txtPasswordText.setText("Incorrect Password");
                txtPasswordText.setStyle("-fx-text-fill: RED"+";");
                txtPasswordText.setVisible(true);
                txtPassword.setVisible(false);
            }
        }else{
            txtUserName.setText("User Not Found");
            txtUserName.setStyle("-fx-text-fill: RED"+";");
        }


    }

    public void moveToPassword(ActionEvent actionEvent) {

        txtPassword.requestFocus();
    }

    public void txtUsernameOnMouseClick(MouseEvent mouseEvent) {
        if (txtUserName.getText().equals("User Not Found")){
            txtUserName.clear();
            txtUserName.setStyle("-fx-text-fill: Black"+";");
        }
    }


    public void PasswordtextOnMouseClick(MouseEvent mouseEvent) {
        txtPasswordText.setVisible(false);
        txtPassword.setVisible(true);
    }
}
