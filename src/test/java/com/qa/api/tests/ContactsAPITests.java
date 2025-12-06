//package com.qa.api.tests;
//
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
//
//import com.qa.api.base.BaseTest;
//import com.qa.api.manager.ConfigManager;
//import com.qa.api.utils.JsonUtils;
//import com.qa.api.utils.ReportManager;
//
//import io.restassured.response.Response;
//
//public class ContactsAPITests extends BaseTest {
//
//    private String getCreatedContactId;
//
//    @BeforeClass
//    public void SetUp() {
//        String bearerToken = ConfigManager.get("BEARER_TOKEN");
//        headers.put("Authorization", "Bearer " + bearerToken);
//    }
//
//    @Test
//    public void createContact() {
//        String payload = JsonUtils.readJson("createContactPayload.json");        
//        test = extent.createTest("POST/ Create New Contact");        
//        Response response = client.post(getBaseURL(),"contacts/", headers, payload);
//
//        // SAFE: POST always returns JSON
//        getCreatedContactId = response.jsonPath().getString("_id");
//
//        logRequestAndResponse("POST", "contacts/", payload, response);        
//
//        try {
//            if (response.getStatusCode() == 201 && 
//                "David".equals(response.path("firstName"))) 
//            {
//                test.pass("Status code is 201 and name validated – PASS");
//            } else {
//                throw new AssertionError("Expected 201 but got: " + response.getStatusCode());
//            }
//        } catch (AssertionError e) {
//            test.fail("Assertion Failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test(dependsOnMethods = {"createContact"})
//    public void getContact() {
//        test = extent.createTest("GET/ Fetch Newly Created Contact");
//        Response response = client.get(getBaseURL(),"contacts/" + getCreatedContactId, headers);
//        logRequestAndResponse("GET", "contacts/" + getCreatedContactId, null, response);
//        try {
//            if (response.getStatusCode() == 200 && 
//                "David".equals(response.path("firstName"))) 
//            {
//                test.pass("Status code is 200 and name validated – PASS");
//            } else {
//                throw new AssertionError("Expected 200 but got: " + response.getStatusCode());
//            }
//        } catch (AssertionError e) {
//            test.fail("Assertion Failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test(dependsOnMethods = {"getContact"})
//    public void putUpdateContact() {
//        String payload = JsonUtils.readJson("putUpdateContactPayload.json");
//        test = extent.createTest("PUT/ Update Contact");
//        Response response = client.put(getBaseURL(),"contacts/" + getCreatedContactId, headers, payload);
//        logRequestAndResponse("PUT", "contacts/" + getCreatedContactId, payload, response);
//        try {
//            if (response.getStatusCode() == 200 && 
//                "David_Update_Put".equals(response.path("firstName"))) 
//            {
//                test.pass("Status code is 200 and updated name validated – PASS");
//            } else {
//                throw new AssertionError("Expected 200 but got: " + response.getStatusCode());
//            }
//        } catch (AssertionError e) {
//            test.fail("Assertion Failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test(dependsOnMethods = {"putUpdateContact"})
//    public void patchUpdateContact() {
//        String payload = JsonUtils.readJson("patchUpdateContactPayload.json");
//        test = extent.createTest("PATCH/ Update Contact");
//        Response response = client.patch(getBaseURL(),"contacts/" + getCreatedContactId, headers, payload);
//        logRequestAndResponse("PATCH", "contacts/" + getCreatedContactId, payload, response);
//        try {
//            if (response.getStatusCode() == 200 &&
//                "David_Update_Patch".equals(response.path("firstName"))) 
//            {
//                test.pass("Status code is 200 and updated name validated – PASS");
//            } else {
//                throw new AssertionError("Expected 200 but got: " + response.getStatusCode());
//            }
//        } catch (AssertionError e) {
//            test.fail("Assertion Failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    @Test(dependsOnMethods = {"patchUpdateContact"})
//    public void deleteContact() {
//        test = extent.createTest("DELETE/ Delete Newly Created Contact");
//        Response response = client.delete(getBaseURL(),"contacts/" + getCreatedContactId, headers);
//        logRequestAndResponse("DELETE", "contacts/" + getCreatedContactId, null, response);
//        try {
//            if (response.getStatusCode() == 200) {
//                test.pass("Status code is 200 – PASS");
//            } else {
//                throw new AssertionError("Expected 200 but got: " + response.getStatusCode());
//            }
//        } catch (AssertionError e) {
//            test.fail("Assertion Failed: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    
//    @AfterClass
//    public void tearDown() {
//        ReportManager.flushReport();
//    }
//}
//


package com.qa.api.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.qa.api.base.BaseTest;
import com.qa.api.manager.ConfigManager;
import com.qa.api.utils.JsonUtils;
import com.qa.api.utils.ReportManager;

import io.restassured.response.Response;
import com.aventstack.extentreports.ExtentTest;

public class ContactsAPITests extends BaseTest {

    private String getCreatedContactId;
    private ExtentTest test;

    @BeforeClass
    public void setUp() {
        String bearerToken = ConfigManager.get("BEARER_TOKEN");
        headers.put("Authorization", "Bearer " + bearerToken);
    }

    @Test
    public void createContact() {
        test = extent.createTest("POST /contacts - Create Contact");
        ReportManager.assignCategory(test, "POST", "Regression");

        String payload = JsonUtils.readJson("createContactPayload.json");
        ReportManager.logRequest(test, "POST", "/contacts", payload);

        Response response = client.post(getBaseURL(), "contacts/", headers, payload);
        getCreatedContactId = response.jsonPath().getString("_id");

        ReportManager.logResponse(test, response);

        boolean condition = response.getStatusCode() == 201 && "David".equals(response.path("firstName"));
        ReportManager.logAssertion(test, condition, "Contact created successfully", "Failed to create contact");
    }

    @Test(dependsOnMethods = {"createContact"})
    public void getContact() {
    	 test = extent.createTest("GET /contacts - Fetch Newly Created Contact");
        ReportManager.assignCategory(test, "GET", "Regression");

        Response response = client.get(getBaseURL(), "contacts/" + getCreatedContactId, headers);
        ReportManager.logRequest(test, "GET", "/contacts/" + getCreatedContactId, null);
        ReportManager.logResponse(test, response);

        boolean condition = response.getStatusCode() == 200 && "David".equals(response.path("firstName"));
        ReportManager.logAssertion(test, condition, "Contact fetched successfully", "Failed to fetch contact");
    }

    @Test(dependsOnMethods = {"getContact"})
    public void putUpdateContact() {
    	test = extent.createTest("PUT /contacts - Update Contact");
        ReportManager.assignCategory(test, "PUT", "Regression");

        String payload = JsonUtils.readJson("putUpdateContactPayload.json");
        ReportManager.logRequest(test, "PUT", "/contacts/" + getCreatedContactId, payload);

        Response response = client.put(getBaseURL(), "contacts/" + getCreatedContactId, headers, payload);
        ReportManager.logResponse(test, response);

        boolean condition = response.getStatusCode() == 200 && "David_Update_Put".equals(response.path("firstName"));
        ReportManager.logAssertion(test, condition, "Contact updated successfully with PUT", "PUT update failed");
    }

    @Test(dependsOnMethods = {"putUpdateContact"})
    public void patchUpdateContact() {
    	test = extent.createTest("PATCH /contacts - Update Contact");
        ReportManager.assignCategory(test, "PATCH", "Regression");

        String payload = JsonUtils.readJson("patchUpdateContactPayload.json");
        ReportManager.logRequest(test, "PATCH", "/contacts/" + getCreatedContactId, payload);

        Response response = client.patch(getBaseURL(), "contacts/" + getCreatedContactId, headers, payload);
        ReportManager.logResponse(test, response);

        boolean condition = response.getStatusCode() == 200 && "David_Update_Patch".equals(response.path("firstName"));
        ReportManager.logAssertion(test, condition, "Contact updated successfully with PATCH", "PATCH update failed");
    }

    @Test(dependsOnMethods = {"patchUpdateContact"})
    public void deleteContact() {
    	test = extent.createTest("DELETE /contacts - Delete Contact");
        ReportManager.assignCategory(test, "DELETE", "Regression");

        ReportManager.logRequest(test, "DELETE", "/contacts/" + getCreatedContactId, null);

        Response response = client.delete(getBaseURL(), "contacts/" + getCreatedContactId, headers);
        ReportManager.logResponse(test, response);

        boolean condition = response.getStatusCode() == 200;
        ReportManager.logAssertion(test, condition, "Contact deleted successfully", "Failed to delete contact");
    }

    @AfterClass
    public void tearDown() {
        ReportManager.flushReport();
    }
}
