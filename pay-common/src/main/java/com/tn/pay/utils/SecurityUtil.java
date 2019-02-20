package com.tn.pay.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 各种加密解密算法
 */
public class SecurityUtil {

    /**
     * 加密解密默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * MD5 加密
     */
    public static String MD5(String str) throws Exception {
        if (str == null)
            return null;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(str.getBytes(DEFAULT_CHARSET));
        byte[] digest = md5.digest();
        StringBuffer hexString = new StringBuffer();
        String strTemp;
        for (int i = 0; i < digest.length; i++) {
            strTemp = Integer.toHexString((digest[i] & 0x000000FF) | 0xFFFFFF00).substring(6);
            hexString.append(strTemp);
        }
        return hexString.toString();
    }

    // ==Base64加解密==================================================================

    /**
     * Base64加密
     */
    public static String Base64Encode(String str) throws IOException {
        return new String(new Base64().encode(str.getBytes(DEFAULT_CHARSET)));
    }

    /**
     * Base64解密
     */
    public static String Base64Decode(String str) throws IOException {
        return new String(new Base64().decode(str), DEFAULT_CHARSET);
    }

    // ==Aes加解密==================================================================

    /**
     * aes解密-128位
     */
    public static String AesDecrypt(String encryptContent, String password) throws Exception {
        if (StringUtils.isEmpty(password) || password.length() != 16) {
            throw new RuntimeException("密钥长度为16位");
        }
        String key = password;
        String iv = password;
        byte[] encrypted1 = StringUtil.hex2Bytes(encryptContent);
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted1);
        return new String(original, DEFAULT_CHARSET).trim();
    }

    /**
     * aes加密-128位
     */
    public static String AesEncrypt(String content, String key) throws Exception {
        if (StringUtils.isEmpty(key) || key.length() != 16) {
            throw new RuntimeException("密钥长度为16位");
        }
        String iv = key;
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        int blockSize = cipher.getBlockSize();
        byte[] dataBytes = content.getBytes(DEFAULT_CHARSET);
        int plaintextLength = dataBytes.length;
        if (plaintextLength % blockSize != 0) {
            plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
        }
        byte[] plaintext = new byte[plaintextLength];
        System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(plaintext);
        return StringUtil.byte2Hex(encrypted);
    }

    // ==DES加解密==================================================================

    /**
     * DES加密
     */
    public static String desEncrypt(String source, String desKey) throws Exception {
        // 从原始密匙数据创建DESKeySpec对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(new DESKeySpec(desKey.getBytes()));
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey);
        // 现在，获取数据并加密
        byte[] destBytes = cipher.doFinal(source.getBytes());
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : destBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * DES解密
     */
    public static String desDecrypt(String source, String desKey) throws Exception {
        // 解密数据
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(new DESKeySpec(desKey.getBytes()));
        Cipher cipher = Cipher.getInstance("DES");
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey);
        // 现在，获取数据并解密
        byte[] destBytes = cipher.doFinal(sourceBytes);
        return new String(destBytes);
    }

    // ==3DES加解密==================================================================

    /**
     * 3DES加密
     */
    public static String threeDesEncrypt(String src, String key) throws Exception {
        return StringUtil.byte2Hex(threeDesEncrypt(src.getBytes(), key.getBytes()));
    }

    /**
     * 3DES解密
     */
    public static String threeDesDecrypt(String src, String key) throws Exception {
        return new String(threeDesDecrypt(StringUtil.hex2Bytes(src), key.getBytes()));
    }

    /**
     * 3DES加密
     */
    private static byte[] threeDesEncrypt(byte[] src, byte[] keybyte) throws Exception {
        // 生成密钥
        byte[] key = new byte[24];
        if (keybyte.length < key.length) {
            System.arraycopy(keybyte, 0, key, 0, keybyte.length);
        } else {
            System.arraycopy(keybyte, 0, key, 0, key.length);
        }
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        // 加密
        Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

    /**
     * 3DES解密
     */
    private static byte[] threeDesDecrypt(byte[] src, byte[] keybyte) throws Exception {
        // 生成密钥
        byte[] key = new byte[24];
        if (keybyte.length < key.length) {
            System.arraycopy(keybyte, 0, key, 0, keybyte.length);
        } else {
            System.arraycopy(keybyte, 0, key, 0, key.length);
        }
        SecretKey deskey = new SecretKeySpec(key, "DESede");
        // 解密
        Cipher c1 = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        c1.init(Cipher.DECRYPT_MODE, deskey);
        return c1.doFinal(src);
    }

    // ==sha-1加密==================================================================

    /**
     * 宝付签名方法
     * sha-1计算后进行16进制转换
     */
    public static String sha1X16(String data, String encoding) throws Exception {
        byte[] bytes = sha1(data.getBytes(encoding));
        return StringUtil.byte2Hex(bytes);
    }

    /**
     * sha-1计算.
     */
    public static byte[] sha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.reset();
        md.update(data);
        return md.digest();
    }


    public static void main(String[] args) throws Exception {
        String str = "数据加密的基本过程就是对原来为明文的文件或数据按某种算法进行处理，使其成为不可读的一段代码，通常称为“密文”，"
                + "使其只能在输入相应的密钥之后才能显示出本来内容，通过这样的途径来达到保护数据不被非法人窃取、阅读的目的。 "
                + "该过程的逆过程为解密，即将该编码信息转化为其原来数据的过程。";
        str += str;
        str += str;
        str += str;
        String PWD = "SecurityUtil.PWD";
        System.out.println("原文:[" + str.length() + "]" + str);
        System.out.println("==MD5===============");
        System.out.println(MD5(str));
        System.out.println("==Base64============");
        String strBase64 = Base64Encode(str);
        System.out.println("加密:[" + strBase64.length() + "]" + strBase64);
        System.out.println("解密:" + Base64Decode(strBase64));
        System.out.println("==Aes============");
        String strAes = AesEncrypt(str, PWD);
        System.out.println("加密:[" + strAes.length() + "]" + strAes);
        System.out.println("解密:" + AesDecrypt(strAes, PWD));
        System.out.println("==Des==============");
        String strDes = desEncrypt(str, PWD);
        System.out.println("加密:[" + strDes.length() + "]" + strDes);
        System.out.println("解密:" + desDecrypt(strDes, PWD));
        System.out.println("==3Des==============");
        String str3Des = threeDesEncrypt(str, PWD);
        System.out.println("加密:[" + str3Des.length() + "]" + str3Des);
        System.out.println("解密:" + threeDesDecrypt(str3Des, PWD));

        //==========================================

        long t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            MD5(str);
        System.out.println("\nMD5:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            Base64Encode(str);
        System.out.println("Base64:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            AesEncrypt(str, PWD);
        System.out.println("Aes:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            desEncrypt(str, PWD);
        System.out.println("Des:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            threeDesEncrypt(str, PWD);
        System.out.println("3Des:" + (System.currentTimeMillis() - t1));
        //=======================================
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            Base64Decode(strBase64);
        System.out.println("\nBase64:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            AesDecrypt(strAes, PWD);
        System.out.println("Aes:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            desDecrypt(strDes, PWD);
        System.out.println("Des:" + (System.currentTimeMillis() - t1));
        t1 = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++)
            threeDesDecrypt(str3Des, PWD);
        System.out.println("3Des:" + (System.currentTimeMillis() - t1));
    }

}
