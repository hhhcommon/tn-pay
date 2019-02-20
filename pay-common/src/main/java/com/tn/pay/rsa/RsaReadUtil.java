package com.tn.pay.rsa;

import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;

/**
 * 读取公钥私钥
 */
public final class RsaReadUtil {

    /**
     * 根据Cer文件读取公钥
     */
    public static PublicKey getPublicKeyFromFile(String pubCerPath) throws Exception {
        FileInputStream pubKeyStream = null;
        try {
            pubKeyStream = new FileInputStream(pubCerPath);
            byte[] reads = new byte[pubKeyStream.available()];
            pubKeyStream.read(reads);
            return getPublicKeyByText(new String(reads));
        } finally {
            if (pubKeyStream != null) {
                pubKeyStream.close();
            }
        }
    }

    /**
     * 根据公钥Cer文本串读取公钥
     */
    public static PublicKey getPublicKeyByText(String pubKeyText) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(RsaConst.KEY_X509);
        BufferedReader br = new BufferedReader(new StringReader(pubKeyText));
        String line;
        StringBuilder keyBuffer = new StringBuilder();
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("-")) {
                keyBuffer.append(line);
            }
        }
        InputStream is = new ByteArrayInputStream(new Base64().decode(keyBuffer.toString()));
        Certificate certificate = certificateFactory.generateCertificate(is);
        return certificate.getPublicKey();
    }


    /**
     * 根据私钥路径读取私钥
     */
    public static PrivateKey getPrivateKeyFromFile(String pfxPath, String priKeyPass) throws Exception {
        InputStream priKeyStream = null;
        try {
            priKeyStream = new FileInputStream(pfxPath);
            byte[] reads = new byte[priKeyStream.available()];
            priKeyStream.read(reads);
            return getPrivateKeyByStream(reads, priKeyPass);
        } finally {
            if (priKeyStream != null) {
                priKeyStream.close();
            }
        }
    }

    /**
     * 根据PFX私钥字节流读取私钥
     */
    public static PrivateKey getPrivateKeyByStream(byte[] pfxBytes, String priKeyPass) throws Exception {
        KeyStore ks = KeyStore.getInstance(RsaConst.KEY_PKCS12);
        char[] charPriKeyPass = priKeyPass.toCharArray();
        ks.load(new ByteArrayInputStream(pfxBytes), charPriKeyPass);
        Enumeration<String> aliasEnum = ks.aliases();
        String keyAlias = null;
        if (aliasEnum.hasMoreElements()) {
            keyAlias = aliasEnum.nextElement();
        }
        return (PrivateKey) ks.getKey(keyAlias, charPriKeyPass);
    }

    public static void main(String[] args) throws Exception {
        String path = RsaReadUtil.class.getResource("/").getPath();
        PublicKey publicKey = getPublicKeyFromFile(path + "pubKey.cer");
        System.out.println("pub: " + publicKey);
        PrivateKey privateKey = getPrivateKeyFromFile(path + "priKey.pfx", "123456");
        System.out.println("pri: " + privateKey);
    }

}
