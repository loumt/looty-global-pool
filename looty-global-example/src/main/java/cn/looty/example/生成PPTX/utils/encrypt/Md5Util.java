package cn.looty.example.生成PPTX.utils.encrypt;

import java.security.MessageDigest;


/**
 * 
 * @DESCRIPTION:MD5加密
 * @TODO:未实现的事情
 * @author xiaokong
 * @date 2018年3月24日
 * @version 1.0.0
 * @file com.frame.util.Md5.java
 */
public class Md5Util {
    /**
     * 
     * @DESCRIPTION:加密
     * @TODO:未实现的事情
     * @param plainText 明文
     * @return 32位密文
     */
    public static String encryption(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes("UTF-8"));
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();

        } catch (Exception e) {

        }
        return null;
    }
}
