package kr.co.teamo.auth.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class AesEncryptor {

    @Value("${aes.secret-key}")
    private String secretKey;

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // 암호화
    public String encrypt(String value) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            // 1️⃣ 키
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");

            // 2️⃣ IV 생성 (16byte 랜덤)
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 3️⃣ 암호화
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());

            // 4️⃣ IV + 암호문 합치기
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("암호화 실패", e);
        }
    }

    // 🔓 복호화
    public String decrypt(String encryptedValue) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            byte[] combined = Base64.getDecoder().decode(encryptedValue);

            // 1️⃣ IV 추출
            byte[] iv = new byte[16];
            byte[] encrypted = new byte[combined.length - 16];

            System.arraycopy(combined, 0, iv, 0, 16);
            System.arraycopy(combined, 16, encrypted, 0, encrypted.length);

            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            // 2️⃣ 키
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");

            // 3️⃣ 복호화
            cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
            byte[] decrypted = cipher.doFinal(encrypted);

            return new String(decrypted);

        } catch (Exception e) {
            throw new RuntimeException("복호화 실패", e);
        }
    }
}