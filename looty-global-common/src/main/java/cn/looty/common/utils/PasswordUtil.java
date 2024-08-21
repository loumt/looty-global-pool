package cn.looty.common.utils;

import cn.hutool.core.lang.UUID;
import org.springframework.util.DigestUtils;

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


    /**
     * 生成加密盐
     * @return
     */
    public static String generalSalt(){
         return UUID.randomUUID().toString().replace("-", "");
    }

    public static String encrypt(String password, String salt){
        return DigestUtils.md5DigestAsHex((password + salt).getBytes());
    }

    public static void main(String[] args) {
        String salt = generalSalt();
        System.out.println("salt:" + salt);
        String encrypt = encrypt("123456", "176feba0b94b454c8ae223c9347e2333");
        System.out.println(encrypt);
    }


}
