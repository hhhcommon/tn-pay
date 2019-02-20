package com.tn.pay.rsa;

import cfca.sadk.algorithm.common.Mechanism;
import cfca.sadk.lib.crypto.JCrypto;
import cfca.sadk.lib.crypto.Session;
import cfca.sadk.util.EncryptUtil;
import com.tn.pay.utils.StringUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 公钥私钥相关的加密解密
 */
public final class RsaCodingUtil {

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    // ======================================================================================
    // 公钥加密私钥解密段
    // ======================================================================================

    /**
     * 指定Cer公钥路径加密
     *
     * @src 明文
     * @pubCerPath 公钥路径
     */
    public static String encryptByPubCerFile(String src, String pubCerPath) throws Exception {
        PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);
        if (publicKey == null) {
            return null;
        }
        return encryptByPublicKey(src, publicKey);
    }

    /**
     * 用公钥内容加密
     *
     * @src 明文
     * @pubKeyText 公钥内容
     */
    public static String encryptByPubCerText(String src, String pubKeyText) throws Exception {
        PublicKey publicKey = RsaReadUtil.getPublicKeyByText(pubKeyText);
        if (publicKey == null) {
            return null;
        }
        return encryptByPublicKey(src, publicKey);
    }

    /**
     * 公钥加密返回
     *
     * @src 明文
     * @publicKey 公钥
     */
    public static String encryptByPublicKey(String src, PublicKey publicKey) throws Exception {
        byte[] destBytes = rsaByKey(src.getBytes(), publicKey, Cipher.ENCRYPT_MODE);
        if (destBytes == null) {
            return null;
        }
        return StringUtil.byte2Hex(destBytes);
    }

    /**
     * 根据私钥文件解密
     *
     * @src 密文
     * @pfxPath 私钥路径
     * @priKeyPass 私钥密码
     */
    public static String decryptByPriPfxFile(String src, String pfxPath, String priKeyPass) throws Exception {
        if (StringUtils.isEmpty(src) || StringUtils.isEmpty(pfxPath)) {
            return null;
        }
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
        if (privateKey == null) {
            return null;
        }
        return decryptByPrivateKey(src, privateKey);
    }

    /**
     * 根据私钥文件流解密
     *
     * @src 密文
     * @pfxBytes 私钥文件流
     * @priKeyPass 私钥密码
     */
    public static String decryptByPriPfxStream(String src, byte[] pfxBytes, String priKeyPass) throws Exception {
        if (StringUtils.isEmpty(src)) {
            return null;
        }
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyByStream(pfxBytes, priKeyPass);
        if (privateKey == null) {
            return null;
        }
        return decryptByPrivateKey(src, privateKey);
    }

    /**
     * 私钥解密
     *
     * @src 密文
     * @privateKey 私钥
     */
    public static String decryptByPrivateKey(String src, PrivateKey privateKey) throws Exception {
        if (StringUtils.isEmpty(src)) {
            return null;
        }
        byte[] destBytes = rsaByKey(StringUtil.hex2Bytes(src), privateKey, Cipher.DECRYPT_MODE);
        if (destBytes == null) {
            return null;
        }
        return new String(destBytes, RsaConst.ENCODE);
    }

    // ======================================================================================
    // 私钥加密公钥解密
    // ======================================================================================

    /**
     * 根据私钥文件加密
     *
     * @src 明文
     * @pfxPath 私钥路径
     * @priKeyPass 私钥密码
     */
    public static String encryptByPriPfxFile(String src, String pfxPath, String priKeyPass) throws Exception {
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
        if (privateKey == null) {
            return null;
        }
        return encryptByPrivateKey(src, privateKey);

    }

    /**
     * 根据私钥文件流加密
     *
     * @src 明文
     * @pfxBytes 私钥文件流
     * @priKeyPass 私钥密码
     */
    public static String encryptByPriPfxStream(String src, byte[] pfxBytes, String priKeyPass) throws Exception {
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyByStream(pfxBytes, priKeyPass);
        if (privateKey == null) {
            return null;
        }
        return encryptByPrivateKey(src, privateKey);
    }

    /**
     * 根据私钥加密
     *
     * @src 明文
     * @privateKey 私钥
     */
    public static String encryptByPrivateKey(String src, PrivateKey privateKey) throws Exception {
        byte[] destBytes = rsaByKey(src.getBytes(), privateKey, Cipher.ENCRYPT_MODE);
        if (destBytes == null) {
            return null;
        }
        return StringUtil.byte2Hex(destBytes);
    }

    /**
     * 指定Cer公钥路径解密
     *
     * @src 密文
     * @pubCerPath 公钥路径
     */
    public static String decryptByPubCerFile(String src, String pubCerPath) throws Exception {
        PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);
        if (publicKey == null) {
            return null;
        }
        return decryptByPublicKey(src, publicKey);
    }

    /**
     * 根据公钥文本解密
     *
     * @src 密文
     * @pubKeyText 公钥内容
     */
    public static String decryptByPubCerText(String src, String pubKeyText) throws Exception {
        PublicKey publicKey = RsaReadUtil.getPublicKeyByText(pubKeyText);
        if (publicKey == null) {
            return null;
        }
        return decryptByPublicKey(src, publicKey);
    }

    /**
     * 根据公钥解密
     *
     * @src 密文
     * @publicKey 公钥
     */
    public static String decryptByPublicKey(String src, PublicKey publicKey) throws Exception {
        byte[] destBytes = rsaByKey(StringUtil.hex2Bytes(src), publicKey, Cipher.DECRYPT_MODE);
        if (destBytes == null) {
            return null;
        }
        return new String(destBytes, RsaConst.ENCODE);
    }

    // ======================================================================================
    // 密钥算法
    // ======================================================================================

    /**
     * 密钥算法
     */
    public static byte[] rsaByKey(byte[] srcData, Key key, int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(RsaConst.RSA_CHIPER);
        cipher.init(mode, key);
        // 分段加密
        int blockSize = (mode == Cipher.ENCRYPT_MODE) ? RsaConst.ENCRYPT_KEYSIZE : RsaConst.DECRYPT_KEYSIZE;
        byte[] decryptData = null;
        for (int i = 0; i < srcData.length; i += blockSize) {
            byte[] doFinal = cipher.doFinal(ArrayUtils.subarray(srcData, i, i + blockSize));
            decryptData = ArrayUtils.addAll(decryptData, doFinal);
        }
        return decryptData;
    }

    //------------------------------------------------------------------------
    // cfca 公钥加密私钥解密
    //------------------------------------------------------------------------

    /**
     * 给敏感信息加密
     *
     * @param msgContent 明文
     * @param pubCerPath 证书路径
     * @return 密文
     */
    public static String encryptByCFCA(String msgContent, String pubCerPath) throws Exception {
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        Session session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        // 加密算法："RSA/ECB/PKCS1PADDING"
        Mechanism mechanism = new Mechanism(Mechanism.RSA_PKCS);
        PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);

        // RSA公钥加密：长度通常限定（1024密钥为117字节内；2048密钥为245字节内）
        byte[] base64Bytes = EncryptUtil.encrypt(mechanism, publicKey, msgContent.getBytes(DEFAULT_CHARSET), session);
        return new String(base64Bytes);

    }

    /**
     * 给敏感信息解密
     *
     * @param msgContent 密文
     * @param pfxPath    证书路径
     * @param priKeyPass 证书密码
     * @return String       明文
     */
    public static String decoderByCFCA(String msgContent, String pfxPath, String priKeyPass) throws Exception {
        if (msgContent == null || "".equals(msgContent.trim())) {
            return "";
        }
        JCrypto.getInstance().initialize(JCrypto.JSOFT_LIB, null);
        Session session = JCrypto.getInstance().openSession(JCrypto.JSOFT_LIB);
        // 加密算法："RSA/ECB/PKCS1PADDING"
        Mechanism mechanism = new Mechanism(Mechanism.RSA_PKCS);
        //读取私钥证书
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);

        // RSA私钥解密：长度通常限定（1024密钥为128字节；2048密钥为256字节）
        byte[] decryptText = EncryptUtil.decrypt(mechanism, privateKey, msgContent.getBytes(DEFAULT_CHARSET), session);
        return new String(decryptText, DEFAULT_CHARSET);
    }
}
