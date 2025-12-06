package com.qa.api.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

public class RestClient {

	private Response sendRequest(String requestName, String baseURL, String endPoint, Map<String, String> headers,
			Object payload) {

		RequestSpecification request = RestAssured.given().baseUri(baseURL).headers(headers).log().all();

		if (payload != null) {
			request.body(payload);
		}

		Response response;

		switch (requestName.toUpperCase()) {
		case "GET":
			response = request.when().get(endPoint);
			break;
		case "POST":
			response = request.when().post(endPoint);
			break;
		case "PUT":
			response = request.when().put(endPoint);
			break;
		case "PATCH":
			response = request.when().patch(endPoint);
			break;
		case "DELETE":
			response = request.when().delete(endPoint);
			break;
		default:
			throw new IllegalArgumentException("Unsupported HTTP method --> " + requestName);
		}

		response.then().log().all();
		return response;
	}

	public Response get(String baseURL, String endPoint, Map<String, String> headers) {
		return sendRequest("GET", baseURL, endPoint, headers, null);
	}

	public Response post(String baseURL, String endPoint, Map<String, String> headers, Object payload) {
		return sendRequest("POST", baseURL, endPoint, headers, payload);
	}

	public Response put(String baseURL, String endPoint, Map<String, String> headers, Object payload) {
		return sendRequest("PUT", baseURL, endPoint, headers, payload);
	}

	public Response patch(String baseURL, String endPoint, Map<String, String> headers, Object payload) {
		return sendRequest("PATCH", baseURL, endPoint, headers, payload);
	}

	public Response delete(String baseURL, String endPoint, Map<String, String> headers) {
		return sendRequest("DELETE", baseURL, endPoint, headers, null);
	}
}
