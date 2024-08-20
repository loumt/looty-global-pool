package cn.looty.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordUtil {
    public static void checkPassword(String psw) {
        if (psw.length() < 8) {
            System.out.println("密码长度过小");
        }
        int count = 0;
        String reg = "[a-z]";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(psw);
        if (matcher.find()) {
            count++;
        }

        reg = "[A-Z]";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(psw);
        if (matcher.find()) {
            count++;
        }

        reg = "[0-9]";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(psw);
        if (matcher.find()) {
            count++;
        }

        reg = "[-|/|*|#|^|&|+|_|=|!|@]";
        pattern = Pattern.compile(reg);
        matcher = pattern.matcher(psw);
        if (matcher.find()) {
            count++;
        }
        if (count < 3) {
            System.out.println("大写、小写、数字、特殊符号需要同时满足任意三种");
        }

    }

}
