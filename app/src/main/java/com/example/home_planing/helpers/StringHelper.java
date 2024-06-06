package com.example.home_planing.helpers;

public class StringHelper {

    public static boolean regexEmailValidationPattern(String email) { // validar formatro de email
        String regex = "([a-zA-Z0-9]+(?:[._+-][a-zA-Z0-9]+)*)@([a-zA-Z0-9]+(?:[.-][a-zA-Z0-9]+)*[.][a-zA-Z]{2,})";
        return email.matches(regex);
    }

    public static boolean regexPasswordValidationPattern(String password) { // validar formatro de password
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return password.matches(regex);
    }
}