package com.example.demo.utils;

import com.example.demo.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;


public class Utils {
    public static Properties loadProperties(String fileName) {
        try (InputStream input = User.class.getClassLoader().getResourceAsStream(fileName)) {

            Properties prop = new Properties();

            if (input == null) {
                throw new IOException();
            }
            prop.load(input);
            return prop;

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String uuidGenerator() {
        return UUID.randomUUID().toString();
    }

}
