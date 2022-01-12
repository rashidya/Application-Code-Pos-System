package lk.ijse.pos_system.validation;

import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Validation {
    public static Pattern personName=Pattern.compile("^([A-z\\s. ]{3,80})$");
    public static Pattern text=Pattern.compile("^([A-z0-9/,\\s]{3,})$");
    public static Pattern number = Pattern.compile("^([0-9]{1,})$");
    public static Pattern decimalNumber = Pattern.compile("^([0-9.]{1,})$");
    public static Pattern city=Pattern.compile("^([A-z]{3,80})$");
    public static Pattern postal=Pattern.compile("^([0-9]{5})$");

    public static boolean validate(HashMap<TextField,Pattern> map){

        for (TextField textField : map.keySet()) {
            Pattern pattern = map.get(textField);
            if (!pattern.matcher(textField.getText()).matches()){
                textField.setStyle("-fx-border-color: red;"+"-fx-border-width: 2;");
                return false;
            }else {
                textField.setStyle("-fx-border-color: green;"+"-fx-border-width: 2;");
            }
        }
        return true;
    }



}
