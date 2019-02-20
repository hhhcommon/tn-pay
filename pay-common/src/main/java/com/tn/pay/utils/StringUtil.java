package com.tn.pay.utils;

import java.util.Random;
import java.util.UUID;

public class StringUtil {

    /**
     * 返回唯一串，格式32位字符
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成随机数字和字母
     */
    public static String getStringRandom(int length) {
        StringBuilder val = new StringBuilder();
        Random random = new Random();
        //length为几位密码
        for (int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            //输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val.append((char) (random.nextInt(26) + temp));
            } else {
                val.append(String.valueOf(random.nextInt(10)));
            }
        }
        return val.toString();
    }

    /**
     * 将byte[] 转换成字符串
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }

    /**
     * 数字左边补0
     *
     * @str 要补0的数，
     * @length 补0后的长度
     */
    public static String leftPad(String str, int length) {
        return String.format("%0" + length + "d", str);
    }

    /**
     * 字符串补0
     *
     * @str 原字符串
     * @strLength 返回长度
     * @flag true 左补0 false 右补0
     */
    public static String addZeroForNum(String str, int strLength, boolean flag) {
        int strLen = str.length();
        if (strLen > strLength) {
            return str.substring(0, 13);
        }
        StringBuffer sb = new StringBuffer();
        while (strLen < strLength) {
            if (flag) {
                sb.append("0").append(str);// 左补0
            } else {
                sb.append(str).append("0");// 右补0
            }
            str = sb.toString();
            strLen = str.length();
        }
        return str;
    }

    /**
     * 小数 转 百分数
     */
    public static String toPercent(Double str) {
        StringBuffer sb = new StringBuffer(Double.toString(str * 100.0000d));
        return sb.append("%").toString();
    }

    /**
     * 百分数 转 小数
     */
    public static Double toDouble(String str) {
        if (str.charAt(str.length() - 1) == '%')
            return Double.parseDouble(str.substring(0, str.length() - 1)) / 100.0000d;
        return 0d;
    }

    /**
     * 获取对称密钥
     *
     * @keyStr 对称密钥字符串
     */
    public static String getAesKey(String keyStr) throws Exception {
        String[] listKeyObj = keyStr.split("\\|");
        if (listKeyObj.length == 2) {
            if (!listKeyObj[1].trim().isEmpty()) {
                return listKeyObj[1];
            } else {
                throw new Exception("Key is Null!");
            }
        } else {
            throw new Exception("Data format is incorrect!");
        }
    }

    public static void main(String[] args) {
        String uuid = getStringRandom(16);
        System.out.println(uuid);
    }

}
