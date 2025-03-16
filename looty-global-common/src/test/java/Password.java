import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SM4;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @Filename: Password
 * @Description:
 * @Version: 1.0.0
 * @Author: louMT
 * @Email: looty_loumt@hotmail.com
 * @Date: 2024-08-21 15:32
 */
public class Password {

    public static void main(String[] args) {
        String content = "test中文frfewrewrwerwer---------------------------------------------------";
// 生成自定义密钥
        byte[] key = KeyUtil.generateKey(SM4.ALGORITHM_NAME, 128).getEncoded();
        SymmetricCrypto sm4 = SmUtil.sm4(key);
        String encryptHex = sm4.encryptHex(content);
        System.out.println(encryptHex);
        String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
        System.out.println(decryptStr);

    }

}
