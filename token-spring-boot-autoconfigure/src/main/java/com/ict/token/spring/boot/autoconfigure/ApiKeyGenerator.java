package com.ict.token.spring.boot.autoconfigure;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public final class ApiKeyGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiKeyGenerator.class);
    /**
     * 32字节的加密秘钥
     */
    private static final String API_SECRET_KEY = "apisecretkey_&&&P@ssw0Rd!@#**&&&";
    private static final String TOKEN_SECRET_KEY = "tokensecretkey_&P@ssw0Rd!@#**&&&";
    private static SecretKey apiKey;
    private static SecretKey tokenKey;
    private static Cipher cipher;
    static {
        try {
            apiKey = new SecretKeySpec(API_SECRET_KEY.getBytes(), "AES");
            tokenKey = new SecretKeySpec(TOKEN_SECRET_KEY.getBytes(), "AES");
            //算法类型/工作方式/填充方式
            cipher=Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (Exception e) {
            LOGGER.error("初始化异常", e);
        }

    }



    public static String generateOrResolve(String message, Mode mode, Type type) {
        String ret = null;
        SecretKey key = null;
        if (type == Type.API_KEY)
            key = apiKey;
        else if (type == Type.TOKEN)
            key = tokenKey;

        try {
            if (mode == Mode.GENERATE) {
                //指定为加密模式
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] result=cipher.doFinal(message.getBytes());
                ret = Hex.encodeHexString(result);
                cipher.init(Cipher.DECRYPT_MODE,key);
            } else if (mode == Mode.RESOLVE) {
                //解密
                //相同密钥，指定为解密模式
                cipher.init(Cipher.DECRYPT_MODE,key);
                //根据加密内容解密
                byte[] result = cipher.doFinal(Hex.decodeHex(message.toCharArray()));
                ret = new String(result);
            }
        } catch (Exception e) {
            LOGGER.error("加密或解密异常", e);
        }
        return ret;
    }

    public enum Mode {
        /**
         * 生成apiKey模式
         */
        GENERATE,
        /**
         * 解析apiKey模式
         */
        RESOLVE
    }

    public enum Type {
        API_KEY,
        TOKEN
    }
    // 489f2f0c76dd6e228c81ef9168f82737d505b4910f501b7f12fd68e7acc996224e12e022bdaa30fce02063f00e764c06e00ba46237636f21ad4b48bfe8791c21
    public static void main(String[] args) {
        String message = "this is a message";
        String apiKey;
        System.out.println(apiKey = generateOrResolve(message, Mode.GENERATE, Type.API_KEY));
        System.out.println(generateOrResolve(apiKey, Mode.RESOLVE, Type.API_KEY));
    }

}
