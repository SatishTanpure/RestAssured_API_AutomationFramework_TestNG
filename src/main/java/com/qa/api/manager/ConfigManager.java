package com.qa.api.manager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

	private static final Properties properties = new Properties();

//    static {
//        try (InputStream input = ConfigManager.class
//                .getClassLoader()
//                .getResourceAsStream("config.properties")) {
//
//            if (input == null) {
//                throw new RuntimeException("config.properties NOT found in classpath!");
//            }
//
//            properties.load(input);
//
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to load config.properties", e);
//        }
//    }

	static {
		try (InputStream input = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
			if (input == null) {
				throw new RuntimeException("config.properties NOT found in classpath!");
			}
			properties.load(input);
		} catch (IOException e) {
			throw new RuntimeException("Failed to load config.properties", e);
		}
	}

	public static String get(String key) {
		return properties.getProperty(key);
	}

	public static void set(String key, String value) {
		properties.setProperty(key, value);
	}
}
