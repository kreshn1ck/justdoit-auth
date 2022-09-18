package com.ubt.auth.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

public class TokenEncryption {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final String CONCATENATE_SYMBOL = ";";

    private static final String ENCRYPTION_KEY = "ABCDEFGHIJKLMNOP";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CIPHER_TRANSFORMATION = "AES/CBC/PKCS5PADDING";
    private static final String AES_ENCRYPTION_ALGORITHM = "AES";
    private static Cipher cipher;
    private static byte[] key;
    private static SecretKeySpec secretKey;
    private static IvParameterSpec ivparameterspec;

    static {
        try {
            cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
            key = ENCRYPTION_KEY.getBytes(CHARACTER_ENCODING);
            secretKey = new SecretKeySpec(key, AES_ENCRYPTION_ALGORITHM);
            ivparameterspec = new IvParameterSpec(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String plainText) throws Exception {
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
        byte[] cipherText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        Base64.Encoder encoder = Base64.getUrlEncoder();
        String encodedToken = encoder.encodeToString(cipherText);
        return encodedToken;
    }

    public static String decrypt(String encryptedText) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] cipherText = decoder.decode(encryptedText.getBytes(StandardCharsets.UTF_8));
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }

    public static boolean isExpired(String token) throws Exception {
        Date dateFormatted = getDateBySplit(decrypt(token));
        if (new Date().compareTo(dateFormatted) > 0) {
            return true;
        }
        return false;
    }

    public static String formatAndEncrypt(Date date) throws Exception {
        return encrypt(format(date));
    }

    public static Date getDateBySplit(String decryptedToken) throws ParseException {
        String[] tokenElements = decryptedToken.split(CONCATENATE_SYMBOL);
        String tokenDate = tokenElements[0];
        return DATE_FORMAT.parse(tokenDate);
    }

    public static Date getDate(String tokenDate) throws ParseException {
        return DATE_FORMAT.parse(tokenDate);
    }

    public static Long getUserIdBySplit(String decryptedToken) {
        String[] tokenElements = decryptedToken.split(CONCATENATE_SYMBOL);
        String tokenId = tokenElements[1];
        return Long.valueOf(tokenId);
    }

    public static String format(Date date) {
        return DATE_FORMAT.format(date);
    }
}