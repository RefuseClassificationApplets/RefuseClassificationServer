import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

import static com.hsspace.hs.util.Util.decrypt;
import static com.hsspace.hs.util.Util.encrypt;

/**
 * test类
 * Git to： http://hs.mccspace.com:3000/Qing_ning/HSPasswordManagerServer/
 *
 * @TIME 2020/7/14 10:23
 * @AUTHOR 韩硕~
 */

public class test {
    public static void main(String[] args) throws Exception {
        // 把BouncyCastle作为Provider添加到java.security:
        Security.addProvider(new BouncyCastleProvider());
        // 原文:
        String message = "B0ltNM7AZ18Wsy2pWit3";
        // 加密口令:
        String password = "ba3253876aed6bc22d4a6ff53d8406c6ad864195ed144ab5c87621b6c233b548baeae6956df346ec8c17f5ea10f35ee3cbc514797ed7ddd3145464e2a0bab413";
        // 16 bytes随机Salt:
        byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);
        System.out.printf("salt: %032x\n", new BigInteger(1, salt));
        // 加密:
        byte[] data = message.getBytes("UTF-8");
        byte[] encrypted = encrypt(password, salt, data);
        System.out.println("encrypted: " + Base64.getEncoder().encodeToString(encrypted));
        // 解密:
        System.out.println(salt.length);
        salt = new BigInteger(new BigInteger(1, salt).toString(16),16).toByteArray();
        System.out.println(salt.length);

        if(salt.length == 17) {
            byte[] newSalt = new byte[16];
            System.arraycopy(salt,1,newSalt,0,16);
            salt = newSalt;
        }
        byte[] decrypted = decrypt(password, salt, encrypted);
        System.out.println("decrypted: " + new String(decrypted, "UTF-8"));
    }

}
