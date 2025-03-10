package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.model.RetentionSetting;

import java.util.LinkedHashMap;
import java.util.Map;

@LambdaHandler(
		lambdaName = "hello_world",
		roleName = "hello_world-role",
		isPublishVersion = true,
		aliasName = "${lambdas_alias_name}",
		logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> request, Context context) {
		System.out.println("Received request: " + request);

		// Extract HTTP method and path from the request
		String httpMethod = (String) request.get("httpMethod");
		String path = (String) request.get("path");

		// Check if the request matches the /hello GET resource
		if ("GET".equalsIgnoreCase(httpMethod) && "/hello".equals(path)) {
			// Return successful response
			return createResponse(200, "Hello from Lambda");
		} else {
			// Return 400 Bad Request error for any other endpoint or method
			String errorMessage = String.format(
					"Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
					path, httpMethod
			);
			return createResponse(400, errorMessage);
		}
	}

	// Utility method to create a response map
	private Map<String, Object> createResponse(int statusCode, String message) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("statusCode", statusCode);
		response.put("message", message);
		return response;
	}
}