package com.qa.api.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonUtils {

	public static String readJson(String fileName) {
		try {
			return new String(Files.readAllBytes(
					Paths.get(System.getProperty("user.dir") + "/src/test/resources/payloads/" + fileName)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
