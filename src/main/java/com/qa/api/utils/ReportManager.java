//package com.qa.api.utils;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import com.aventstack.extentreports.ExtentReports;
//import com.aventstack.extentreports.reporter.ExtentSparkReporter;
//import com.aventstack.extentreports.reporter.configuration.Theme;
//import com.qa.api.manager.ConfigManager;
//
//public class ReportManager {
//
//	private static ExtentReports extent;
//	private static long suiteStartTime;
//	public static ExtentReports getInstance() {
//
//		if (extent == null) {
//			suiteStartTime = System.currentTimeMillis();
//			String reportPath = System.getProperty("user.dir") + "/target/reports/";
//			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//			String fileName = reportPath + "API_Test_Report_" + timestamp + ".html";
//			
//			ExtentSparkReporter spark = new ExtentSparkReporter(fileName);
//			spark.config().setDocumentTitle("API Automation Report");
//			spark.config().setReportName("API Regression Tests");
//			spark.config().setTheme(Theme.STANDARD);
//			
//			extent = new ExtentReports();
//			extent.attachReporter(spark);
//			extent.setSystemInfo("OS", System.getProperty("os.name")); // Operating System
//			extent.setSystemInfo("Test Environment", "QA");       // Environment
//			extent.setSystemInfo("Test Release", "v1.0");         // Release version
//			extent.setSystemInfo("Base URL", ConfigManager.get("baseURL"));
//			extent.setSystemInfo("Execution Date", new Date().toString());
//			extent.setSystemInfo("Executed By", System.getProperty("user.name"));
//		}
//
//		return extent;
//	}
//	
//	public static void flushReport() {
//        long suiteEndTime = System.currentTimeMillis();
//        long totalTimeMillis = suiteEndTime - suiteStartTime;
//        double totalTimeSeconds = totalTimeMillis / 1000.0;
//
//        // Add execution times to system info
//        extent.setSystemInfo("Execution Start Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suiteStartTime)));
//        extent.setSystemInfo("Execution End Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suiteEndTime)));
//        extent.setSystemInfo("Total Execution Time (Seconds)", String.format("%.2f", totalTimeSeconds) + " Seconds");
//        extent.flush();
//    }
//}


package com.qa.api.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.qa.api.manager.ConfigManager;

import io.restassured.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.core.JsonProcessingException;

public class ReportManager {

    private static ExtentReports extent;
    private static long suiteStartTime;

    /** 
     * Get the ExtentReports instance (singleton) 
     */
    public static ExtentReports getInstance() {
        if (extent == null) {
            suiteStartTime = System.currentTimeMillis();

            String reportPath = System.getProperty("user.dir") + "/target/reports/";
            File reportDir = new File(reportPath);
            if (!reportDir.exists()) reportDir.mkdirs();

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = reportPath + "API_Test_Report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(fileName);
            spark.config().setDocumentTitle("API Automation Report");
            spark.config().setReportName("API Regression Tests");
            spark.config().setTheme(Theme.STANDARD);

            extent = new ExtentReports();
            extent.attachReporter(spark);

            // System info
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));
            extent.setSystemInfo("Test Environment", "QA");
            extent.setSystemInfo("Test Release", "v1.0");
            extent.setSystemInfo("Base URL", ConfigManager.get("baseURL"));
            extent.setSystemInfo("Executed By", System.getProperty("user.name"));
        }
        return extent;
    }

    /** 
     * Flush the report and add execution timing info 
     */
    public static void flushReport() {
        if (extent != null) {
            long suiteEndTime = System.currentTimeMillis();
            double totalTimeSeconds = (suiteEndTime - suiteStartTime) / 1000.0;

            extent.setSystemInfo("Execution Start Time",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suiteStartTime)));
            extent.setSystemInfo("Execution End Time",
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(suiteEndTime)));
            extent.setSystemInfo("Total Execution Time (Seconds)", String.format("%.2f", totalTimeSeconds) + " Seconds");

            extent.flush();
        }
    }

    
    /** 
     * Assign category/tags to a test 
     */
    public static void assignCategory(ExtentTest test, String... categories) {
        for (String category : categories) {
            test.assignCategory(category);
        }
    }
    
    /** 
     * Log API request payload with pretty JSON 
     */
    public static void logRequest(ExtentTest test, String method, String endpoint, String payload) {
        test.info(MarkupHelper.createLabel("REQUEST [" + method + "] - " + endpoint, ExtentColor.BLUE));
        if (payload != null && !payload.isBlank()) {
        	test.info("Request Payload Used: ==>>");
            test.info(MarkupHelper.createCodeBlock(prettyPrintJson(payload)));
        } else {
            test.info("Request Body: <empty>");
        }
    }

    /** 
     * Log API response with status, pretty JSON, and response time
     */
    public static void logResponse(ExtentTest test, Response response) {
        long responseTime = response.getTime();
        int statusCode = response.getStatusCode();

        test.info(MarkupHelper.createLabel("RESPONSE Status Code: " + statusCode, 
                statusCode >= 200 && statusCode < 300 ? ExtentColor.GREEN : ExtentColor.RED));

        test.info("Response Time: " + responseTime + " ms");

        if (responseTime > 2000) { // Example threshold warning
            test.warning("Response took longer than 2 seconds!");
        }

        String responseBody = response.getBody().asString();
        if (responseBody != null && !responseBody.isBlank()) {
        	test.info("Response Body Received: ==>>");
            test.info(MarkupHelper.createCodeBlock(prettyPrintJson(responseBody)));
        } else {
            test.info("Response Body: <empty>");
        }
    }

    /** 
     * Log assertion result 
     */
    public static void logAssertion(ExtentTest test, boolean condition, String successMessage, String failMessage) {
        if (condition) {
            test.pass(MarkupHelper.createLabel(successMessage, ExtentColor.GREEN));
        } else {
            test.fail(MarkupHelper.createLabel(failMessage, ExtentColor.RED));
        }
    }


    /** 
     * Pretty print JSON string using Jackson 
     */
    private static String prettyPrintJson(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object obj = mapper.readValue(json, Object.class);
            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            return writer.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            // If JSON parsing fails, return original string
            return json;
        }
    }
}
