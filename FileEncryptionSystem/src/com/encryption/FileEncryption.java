package com.encryption;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class FileEncryption {
    private static final String ALGORITHM = "AES";

    // Method to encrypt the file
    public static void encrypt(String filePath, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(filePath);
             FileOutputStream fos = new FileOutputStream(filePath + ".enc");
             CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
            byte[] inputBuffer = new byte[64];
            int bytesRead;

            while ((bytesRead = fis.read(inputBuffer)) != -1) {
                cos.write(inputBuffer, 0, bytesRead);
            }
        }
    }

    // Method to decrypt the file
    public static void decrypt(String filePath, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        try (FileInputStream fis = new FileInputStream(filePath);
             FileOutputStream fos = new FileOutputStream(filePath.replace(".enc", "_decrypted"));
             CipherInputStream cis = new CipherInputStream(fis, cipher)) {
            byte[] inputBuffer = new byte[64];
            int bytesRead;

            while ((bytesRead = cis.read(inputBuffer)) != -1) {
                fos.write(inputBuffer, 0, bytesRead);
            }
        }
    }

    // Method to generate AES SecretKey
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        keyGen.init(128); // You can use 192 or 256 bits too
        return keyGen.generateKey();
    }

    public static void main(String[] args) {
        try {
            
            String filePath = "C:\\Users\\Zaid\\Desktop\\Mohd zaid , You are software Engine.txt";

            // Generate and save key
            SecretKey secretKey = generateKey();
            DatabaseConnection.saveKeyToDatabase(secretKey);

            // Encrypt the file
            encrypt(filePath, secretKey);
            System.out.println("File encrypted successfully!");

            // Decrypt the file
            decrypt(filePath + ".enc", secretKey);
            System.out.println("File decrypted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


