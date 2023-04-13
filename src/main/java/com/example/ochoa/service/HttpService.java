package com.example.ochoa.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Service
public class HttpService {

	public static final String HTTP_RESPONSE_BODY = "body";
	public static final String HTTP_RESPONSE_STATUS = "status";
	public static final String HEADER_ACCEPT = "Accept";
	public static final String HEADER_JSON = "application/json";
	public static final String HEADER_CONTENT = "Content-Type";
	public static final String HEADER_AUTHORIZATION = "Authorization";

	@Value("${auth.user}")
	private String user;
	
	@Value("${auth.password}")
	private String password;
	
	
	/**
	 * @param url
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public Map<String, Object> sendRequest(String url, HttpMethod method, String body) throws IOException {

		RequestBody requestBody = null;
        if (StringUtils.hasLength(body)) {
            requestBody = RequestBody.create(
                    body, MediaType.parse("application/json;charset=UTF-8"));
        }
		String bodyResult;
		ResponseBody responseBodyCopy;
		Map<String, Object> result = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();

		OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(5, TimeUnit.MINUTES)
				.readTimeout(5, TimeUnit.MINUTES).build();

		Request request = new Request.Builder().url(url).method(method.toString(), requestBody)
				.addHeader(HEADER_ACCEPT, HEADER_JSON).addHeader(HEADER_CONTENT, HEADER_JSON)
				.addHeader(HEADER_AUTHORIZATION, Credentials.basic(user, password)).build();

		Response response = client.newCall(request).execute();
		result.put(HTTP_RESPONSE_STATUS, response.code());

		responseBodyCopy = response.peekBody(Long.MAX_VALUE);
		bodyResult = responseBodyCopy.string();
		result.put(HTTP_RESPONSE_BODY, objectMapper.readTree(bodyResult));

		return result;
	}

	/**
	 * 
	 * @param url
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public ResponseEntity<?> getDataRequest(String url, HttpMethod method, String body) throws IOException {
		Map<String, Object> result = sendRequest(url, method, body);
		HttpStatusCode status = HttpStatus.valueOf(Integer.valueOf(result.get(HTTP_RESPONSE_STATUS).toString()));
		return ResponseEntity.status(status).body(result.get(HTTP_RESPONSE_BODY));

	}
	
}