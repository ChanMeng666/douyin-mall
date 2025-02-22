package com.qxy.common.tool;

/**
 * @author Gloss66
 * @version 1.0
 * @description: 验证器：校验用户名、手机号、邮箱、密码等关键字符串
 */
public class Validator {
    // 邮箱正则表达式
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_%+-]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}$";
    // 手机正则表达式
    public static final String PHONE_REGEX = "^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$";
    // 密码正则表达式
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[A-Za-z0-9#%_+-=]{8,20}$";
    // 用户名正则表达式
    public static final String USERNAME_REGEX = "^(?=.*[a-z])[A-Za-z0-9#%_+-=]{3,20}$";

    // 验证手机号
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches(PHONE_REGEX);
    }

    // 验证用户名
    public static boolean isValidUsername(String username) {
        return username.matches(USERNAME_REGEX);
    }

    // 验证邮箱地址
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    // 验证密码
    public static boolean isValidPassword(String password) {
        return password.matches(PASSWORD_REGEX);
    }

    public static String getKindOfAccount(String account){
        String str;
        if(Validator.isValidEmail(account)) str = "邮箱";
        else if(Validator.isValidPhoneNumber(account)) str = "手机号";
        else if(Validator.isValidUsername(account)) str = "用户名";
        else str = "";
        return str;
    }
}
