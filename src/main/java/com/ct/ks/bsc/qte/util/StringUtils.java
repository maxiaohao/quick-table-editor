package com.ct.ks.bsc.qte.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class StringUtils {

    private static final Random rnd = new Random();


    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for (int i = 0; i < charArray.length; i++)
            byteArray[i] = (byte) charArray[i];
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++) {
            int val = (md5Bytes[i]) & 0xff;
            if (val < 16)
                hexValue.append("0");
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString().toUpperCase();
    }


    public static String getRandomCapitalLetters(int len) {
        if (len <= 0) {
            return "";
        }
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; i++) {
            ret.append((char) ('A' + rnd.nextInt(26)));
        }
        return ret.toString();
    }

}
