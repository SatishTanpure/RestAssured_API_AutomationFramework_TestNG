package com.qa.api.base;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.qa.api.client.RestClient;
import com.qa.api.manager.ConfigManager;
import com.qa.api.utils.ReportManager;

import io.restassured.response.Response;

public class BaseTest {
	protected RestClient client ;
	protected Map<String, String> headers;
	protected static ExtentReports extent;
	protected static ExtentTest test;
	
	public BaseTest() {
	 client = new RestClient();
	 headers = new HashMap<>();
		headers.put("Accept", "application/json");
		headers.put("Content-Type", "application/json");
	    extent = ReportManager.getInstance();
	}
		
	protected String getBaseURL() {
		return ConfigManager.get("baseURL");
	}
	
	protected void logRequestAndResponse(String requestName, String endpoint, Object payload, Response response) {
		if(test != null) {
			test.info("**Request Method:** "+ requestName);
			test.info("**Request Enpoint:** "+ endpoint);
			if(payload != null) {
				test.info("**Request Body Payload:** "+ payload);
			}
			test.info("**Response Status Code:** "+ response.statusCode());
			test.info("**Response Body:**\n "+ response.getBody().asPrettyString());
		}
		
	}
}
