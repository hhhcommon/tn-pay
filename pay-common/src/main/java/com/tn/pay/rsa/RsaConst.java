package com.tn.pay.rsa;

public final class RsaConst {

    /**
     * 令牌编码
     */
    public final static String ENCODE = "UTF-8";

    public final static String KEY_X509 = "X509";//公钥
    public final static String KEY_PKCS12 = "PKCS12";//私钥

    public final static String KEY_ALGORITHM = "RSA";
    public final static String CER_ALGORITHM = "MD5WithRSA";

    public final static String RSA_CHIPER = "RSA/ECB/PKCS1Padding";//算法/工作模式/填充模式

    public final static int KEY_SIZE = 1024;
    /**
     * 1024bit 加密块 大小
     */
    public final static int ENCRYPT_KEYSIZE = 117;
    /**
     * 1024bit 解密块 大小
     */
    public final static int DECRYPT_KEYSIZE = 128;
}
