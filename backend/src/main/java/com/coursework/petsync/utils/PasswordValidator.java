package com.coursework.petsync.utils;

import java.util.regex.Pattern;

/**
 * @author HDH
 * @version 1.0
 */
public class PasswordValidator {

    // 校验密码是否符合要求
    public static boolean isValid(String password) {
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }
        String regex = "(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+";
        return Pattern.matches(regex, password);
    }
}
