package com.encryption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.crypto.SecretKey;

public class DatabaseConnection {
    // Method to save key to the database
    public static void saveKeyToDatabase(SecretKey secretKey) 
    {
        String url = "jdbc:mysql://localhost:3306/fileencryptiondb"; 
        String user = "root"; 
        String password = "XYZ"; 

        try (Connection connection = DriverManager.getConnection(url, user, password)) 
        {
            String sql = "INSERT INTO files (key_value) VALUES (?)";
            
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) 
            {
                pstmt.setBytes(1, secretKey.getEncoded());
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

