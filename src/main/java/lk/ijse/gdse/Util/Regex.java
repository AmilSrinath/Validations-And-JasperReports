package lk.ijse.gdse.Util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Amil Srinath
 */
public class Regex {
    public static boolean isTextFieldValid(TextField textField, String text){
        String filed = "";

        switch (textField){
            case ID:
                filed = "^([A-Z0-9])$";
                break;
            case NAME:
                filed = "^[A-z|\\\\s]{3,}$";
                break;
            case EMAIL:
                filed = "\\b[A-Z0-9._%-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b";
        }

        Pattern pattern = Pattern.compile(filed);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }
}
