package com.example.smsdemo.controllers.utils;

import javafx.scene.control.ToggleGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public static boolean validateName(String name) {
        return name.matches("[A-Z][a-z]*");
    }

    public static boolean validateEmail(String email) {
        Pattern p = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(email);
        return m.find();
    }

    public static boolean validatePwd(String pwd) {
        boolean result = true;
        String pwd_ptrn = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
        Pattern ptrn = Pattern.compile(pwd_ptrn);
        Matcher matcher = ptrn.matcher(pwd);

        if (pwd.length() < 8 || pwd.length() > 20 || !matcher.matches()) {
            result = false;
        }

        return result;
    }

    public static boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile("^\\+\\d{12}$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean isToggleSelected(ToggleGroup toggle) {
        boolean result = true;
        if (toggle.getSelectedToggle() == null) result = false;
        return result;
    }

    public static boolean isNumeric(String number){

        boolean result = true;
        for (int i=0; i<number.length(); i++){
            if (!Character.isDigit(number.charAt(i))){
                result = false;
                break;
            }
        }

        return result;
    }
}
