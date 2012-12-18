package org.swas.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 25.11.11
 */
public class DigestUtil {
    public static String getMd5Digest(String str) {
        return getDigester(str, "MD5");
    }

    public static String getSha256Digest(String str) {
        return getDigester(str, "SHA-256");
    }

    private static String getDigester(String str, String algorithmCode) {
        if (null == str) {
            return null;
        }

        StringBuffer hexString;
        try {
            MessageDigest algorithm = MessageDigest.getInstance(algorithmCode);
            byte[] defaultBytes = str.getBytes();
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            hexString = new StringBuffer();
            for (byte digest : messageDigest) {
                String hex = Integer.toHexString(
                        0xFF & digest);
                if (1 == hex.length()) {
                    hexString.append('0');
                }

                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }

        return null;
    }
}
