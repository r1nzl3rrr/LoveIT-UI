/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group03.loveit.utilities;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SHA-512 algorithm for hashing sensitive string
 *
 * @author duyvu
 */
public final class CryptoUtils {

    // =============================
    // == Fields
    // =============================
    /**
     * Set default size of the salt to 16
     */
    private static final int SALT_SIZE = 16;

    // =============================
    // == Private Methods
    // =============================
    /**
     * Generate random to encrypt the msg - To simplify the process, i will use
     * static salt only
     *
     * @return
     */
    private static byte[] generateSalt() {
        byte[] salt = new byte[SALT_SIZE];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Combining 2 arrays into 1 byte array
     *
     * @param array1
     * @param array2
     * @return
     */
    private static byte[] concatenateArrays(byte[] array1, byte[] array2) {
        byte[] result = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, result, 0, array1.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    // =============================
    // == Public Methods
    // =============================
    /**
     * Hash and Salt the message using salt values
     *
     * @param rawString
     * @param salt
     * @return
     */
    public static String hashAndSalt(String rawString, byte[] salt) {
        try {
            // Combine raw bytes array + trailing salt byte arrays
            byte[] combined = concatenateArrays(rawString.getBytes(), salt);

            // Encrypt using sha 512
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = messageDigest.digest(combined);

            // Combining the leading salt byte arrays + hashed bytes again as the identity
            // Encode the combination using base64
            return Base64.getEncoder().encodeToString(concatenateArrays(salt, hashedBytes));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing and salting", e);
        }
    }

    /**
     * Verify
     *
     * @param enteredString
     * @param storedBytes
     * @return
     */
    public static boolean verify(String enteredString, byte[] storedBytes) {

        byte[] decodedStoredBytes = decodeBase64(storedBytes);
        byte[] enteredBytes = enteredString.getBytes(StandardCharsets.UTF_8);
        byte[] salt = new byte[SALT_SIZE];

        // Copy the salt bytes from encoded leading salt + hashed bytes
        System.arraycopy(decodedStoredBytes, 0, salt, 0, SALT_SIZE);

        // Generate the hashed enteredString to match with storedBytes
        byte[] combined = concatenateArrays(enteredBytes, salt);

        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error verifying password", e);
        }

        byte[] hashedBytes = messageDigest.digest(combined);

        return MessageDigest.isEqual(concatenateArrays(salt, hashedBytes), decodedStoredBytes);
    }

    /**
     * Wrapper class for hashAndSalt method to hidden the salt generated
     *
     * @param rawString
     * @return
     */
    public static String encode(String rawString) {
        byte[] salt = generateSalt();
        return hashAndSalt(rawString, salt);
    }

    /**
     * Decode into base64
     *
     * @param encodedString
     * @return
     */
    public static byte[] decodeBase64(byte[] encodedString) {
        return Base64.getDecoder().decode(encodedString);
    }

    /**
     * Encode to base64
     *
     * @param objectBytes
     * @return
     */
    public static String encodeBase64(byte[] objectBytes) {
        return Base64.getEncoder().encodeToString(objectBytes);
    }

    /*
    public static void main(String[] args) {
        // ACTIVE - USER
        String email = "duy@gmail.com";
        String msg = "duy123";

        // ACTIVE - ADMIN
        String email1 = "nhut@gmail.com";
        String msg1 = "nhut123";

        // DISABLE - USER
        String email2 = "duydie@gmail.com";
        String msg2 = "secure456";

        String encodedString = encode(msg2);
        boolean isVerified = verify(msg2, encodedString.getBytes());

        System.out.println("Encoded msg: " + encodedString);
        System.out.println("is verified: " + isVerified);
        System.out.println("Encoded msg Length: " + encodedString.length());
    }
    */
}
