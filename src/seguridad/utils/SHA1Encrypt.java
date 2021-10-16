package seguridad.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SHA1Encrypt {
    public static String encrypt(String string) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] newString = MessageDigest.getInstance("SHA").digest(string.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(newString);
    }
}
