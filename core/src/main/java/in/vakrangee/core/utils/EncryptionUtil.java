package in.vakrangee.core.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Created by Nileshd on 4/25/2016.
 */
public class EncryptionUtil {
    public static final String PUBLIC_KEY_PATH = "public.key";
    private static final String TAG = "My Message";

    public EncryptionUtil() {
    }

    public static byte[] readFileBytes(String filename, Context context) throws IOException {

        InputStream is = context.getAssets().open(PUBLIC_KEY_PATH);
        byte[] fileBytes = new byte[is.available()];
        is.read(fileBytes);
        is.close();

        return fileBytes;
    }


    public static PublicKey readPublicKey(String filename, Context context) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(readFileBytes(filename, context));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(publicSpec);
    }

    public static byte[] encrypt(PublicKey key, byte[] plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(plaintext);
    }

    public static String encryptString(String plainText, Context context) {
        String encryptedText = "";

        if(TextUtils.isEmpty(plainText))
            return encryptedText;

        try {
            PublicKey publicKey = readPublicKey(PUBLIC_KEY_PATH, context);
            byte[] message = plainText.getBytes("UTF8");
            byte[] secret = encrypt(publicKey, message);
            encryptedText = new String(android.util.Base64.encodeToString(secret, android.util.Base64.DEFAULT));
        } catch (Exception ex) {
            System.out.println("Encryption Issue: "+ex.getMessage());
        }
        return encryptedText;
    }

    public static String encryptByte(byte[] plainText, Context context) {
        String encryptedText = "";
        try {
            PublicKey publicKey = readPublicKey(PUBLIC_KEY_PATH, context);
            byte[] secret = encrypt(publicKey, plainText);
            encryptedText = new String(android.util.Base64.encodeToString(secret, android.util.Base64.DEFAULT));
        } catch (Exception ex) {
            ex.getMessage();
        }
        return encryptedText;
    }

    public  static String encodeBase64(byte[] data) {
        return android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
    }

    //region Get MD5 from string
    public static String getMD5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            //throw new RuntimeException(e);
            return null;
        }
    }
    //endregion
}


