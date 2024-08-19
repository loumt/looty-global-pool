package cn.looty.example.生成PPTX.utils.encrypt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import cn.looty.example.生成PPTX.exceptions.BusinessException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 对称加密和解密
 */
public class AesEncryptUtil {

    static {
        //1.构造密钥生成器，指定为AES算法,不区分大小写
        try {
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            keygen.init(128);
        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * keyStr:只能16位字符串
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    @SuppressWarnings("restriction")
    public static String aesEncode(String keyStr, String content) throws BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        // 判断Key是否正确
        if (keyStr == null || keyStr.length() != 16) {
            throw new BusinessException("密匙长度必须为16位");
        }
        if (content == null || "".equals(content) || "null".equals(content)) {
            return null;
        }
        SecretKey key = new SecretKeySpec(keyStr.getBytes("UTF-8"), "AES");
        //根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.ENCRYPT_MODE, key);
        //获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
        byte[] byte_encode = content.getBytes("UTF-8");
        //根据密码器的初始化方式--加密：将数据加密
        byte[] byte_AES = cipher.doFinal(byte_encode);
        //将加密后的数据转换为字符串
        //这里用Base64Encoder中会找不到包
        //解决办法：
        //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
        return new BASE64Encoder().encode(byte_AES);
    }

    /**
     * keyStr:只能16位字符串
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    @SuppressWarnings("restriction")
    public static String aesDncode(String keyStr, String content) throws BadPaddingException, IllegalBlockSizeException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        // 判断Key是否正确
        if (keyStr == null || keyStr.length() != 16) {
            throw new BusinessException("密匙长度必须为16位");
        }
        if (content == null || "".equals(content) || "null".equals(content)) {
            return null;
        }
        SecretKey key = new SecretKeySpec(keyStr.getBytes("UTF-8"), "AES");
        //根据指定算法AES自成密码器
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        //初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
        cipher.init(Cipher.DECRYPT_MODE, key);
        //将加密并编码后的内容解码成字节数组
        byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
        /*
         * 解密
         */
        byte[] byte_decode = cipher.doFinal(byte_content);
        String AES_decode = new String(byte_decode, "utf-8");
        return AES_decode;
    }


}


