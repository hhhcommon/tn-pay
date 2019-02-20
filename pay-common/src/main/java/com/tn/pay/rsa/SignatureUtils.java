package com.tn.pay.rsa;

import com.tn.pay.utils.StringUtil;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 各种签名验签算法
 */
public class SignatureUtils {

    /**
     * 加密算法RSA
     */
    private static final String KEY_ALGORITHM = "RSA";

    /**
     * 签名算法
     */
    private static final String SIGNATURE_ALGORITHM = "SHA1withRSA";

    /**
     * 默认编码
     */
    private static final String DEFAULT_CHARSET = "UTF-8";

    //------------------------------------------------------------------------
    // baofoo
    //------------------------------------------------------------------------

    /**
     * 验签
     *
     * @pubCerPath 公钥路径
     * @encryptStr 摘要
     * @signature 签名
     */
    public static boolean verifySignature(String pubCerPath, String encryptStr, String signature) throws Exception {
        PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);
        return verify(encryptStr.getBytes(DEFAULT_CHARSET), publicKey.getEncoded(), signature);
    }

    /**
     * 校验数字签名
     *
     * @data 已加密数据
     * @keyBytes 公钥
     * @sign 数字签名
     */
    public static boolean verify(byte[] data, byte[] keyBytes, String sign) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicK = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicK);
        signature.update(data);
        return signature.verify(StringUtil.hex2Bytes(sign));
    }

    /**
     * 签名
     *
     * @encryptStr 摘要
     * @pfxPath pfx证书路径
     * @priKeyPass 私钥密码
     */
    public static String encryptByRSA(String encryptStr, String pfxPath, String priKeyPass) throws Exception {
        PrivateKey privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
        return sign(encryptStr.getBytes(DEFAULT_CHARSET), privateKey.getEncoded());
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @data 已加密数据
     * @keyBytes 私钥
     */
    public static String sign(byte[] data, byte[] keyBytes) throws Exception {
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateK = keyFactory.generatePrivate(pkcs8KeySpec);
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(data);
        return StringUtil.byte2Hex(signature.sign());
    }

    //------------------------------------------------------------------------
    // cib
    //------------------------------------------------------------------------

    /**
     * 生成签名MAC字符串
     *
     * @param params     参数列表（不含mac参数）
     * @param pfxPath    证书名称
     * @param priKeyPass 证书密码
     * @return MAC字符串
     */
    public static String generateMAC(Map<String, String> params, String pfxPath, String priKeyPass) throws Exception {

        if (params.containsKey("signType") && params.get("signType").equals("RSA")) {
            //读取私钥证书
            PrivateKey privateKey = RsaReadUtil.getPrivateKeyFromFile(pfxPath, priKeyPass);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initSign(privateKey);
            signature.update(generateParamStr(params).getBytes(DEFAULT_CHARSET));
            byte[] signed = signature.sign();

            // 计算base64encode(signed)，无换行。如无法使用，请自行替换为其它BASE64类库。
            return new BASE64Encoder().encode(signed)
                    .replaceAll(System.getProperty("line.separator"), "");
        } else {
            // 签名类型有误
            throw new Exception("SIGN_TYPE_ERROR");
        }
    }

    /**
     * 验证服务器返回的信息中签名的正确性
     *
     * @param params     参数列表（包含mac参数）
     * @param pubCerPath 证书名称
     * @return true-验签通过，false-验签失败
     */
    public static boolean verifyMAC(Map<String, String> params, String pubCerPath) throws Exception {
        if (!params.containsKey("mac")) {
            return false;
        }

        String mac = params.get("mac");
        if (params.containsKey("signType") && params.get("signType").equals("RSA")) {
            if (mac == null) {
                return false;
            }
            PublicKey publicKey = RsaReadUtil.getPublicKeyFromFile(pubCerPath);
            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
            signature.initVerify(publicKey);
            signature.update(generateParamStr(params).getBytes(DEFAULT_CHARSET));

            // 计算base64decode(mac)。如无法使用，请自行替换为其它BASE64类库。
            byte[] bmac = new BASE64Decoder().decodeBuffer(mac);

            return signature.verify(bmac);
        } else {
            // 签名类型有误
            throw new Exception("SIGN_TYPE_ERROR");
        }
    }

    /**
     * 生成用于MAC计算的参数字符串。<br>
     *
     * @param params 参数列表
     * @return 模式为key=value&key=value
     */
    private static String generateParamStr(Map<String, String> params) {
        // 取所有非空字段内容（除mac以外），塞入列表
        List<String> paramList = new ArrayList<>();
        for (String key : params.keySet()) {
            String val = String.valueOf(params.get(key));
            // 若为空字段内容，则跳过不参与生成签名
            if (isVerify(key)) {
                continue;
            }
            paramList.add(key + "=" + val);
        }

        // 防护
        if (paramList.size() == 0) {
            return null;
        }
        // 对列表进行排序
        Collections.sort(paramList);
        // 以&符分割拼装成字符串
        StringBuilder sb = new StringBuilder();
        sb.append(paramList.get(0));
        for (int i = 1; i < paramList.size(); i++) {
            sb.append("&").append(paramList.get(i));
        }
        return sb.toString();
    }

    /**
     * 请求字段参数是否需要参与签名
     */
    private static boolean isVerify(String key) {
        return key == null || "".equals(key) || "mac".equals(key);
    }
}
