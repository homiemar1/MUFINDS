package com.example.mufinds;

import android.util.Base64;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncriptarContraseña {

    public static SecretKey generarSecretKey(String contraseña , int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
            try {
                byte[] data = contraseña.getBytes("UTF-8");
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hash = md.digest(data);
                byte[] key = Arrays.copyOf(hash, keySize / 8);
                sKey = new SecretKeySpec(key, "AES");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        return sKey;
    }

    public static String encriptarMensaje(String contraseña)  {
        String contraseñaEncriptada = "";
        try {
            SecretKey secret = generarSecretKey(contraseña, 128);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] cipherText = cipher.doFinal(contraseña.getBytes());
            contraseñaEncriptada = Base64.encodeToString(cipherText, Base64.DEFAULT);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return contraseñaEncriptada;
    }
}
