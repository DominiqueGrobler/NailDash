package com.naomi.nail_dash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class Encrypt {
public static String md5(String pass) {
    try {
        // Create MD5 Hash
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.update(pass.getBytes());
        byte messageDigest[] = digest.digest();
  

            StringBuffer encrypString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                encrypString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return encrypString.toString();
  
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace(); 
        } 
        return "";
    }
}