package com.flipfit.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

/**
 * HTTP Client Utility for making requests to DropWizard REST API
 */
public class HttpClientUtil {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    /**
     * Make a POST request to the REST API.
     *
     * @param endpoint the API endpoint path (e.g., "/users/login")
     * @param data     the request body data as a Map
     * @return the response as a Map containing the JSON response
     * @throws IOException if the HTTP request fails or response cannot be parsed
     */
    public static Map<String, Object> post(String endpoint, Map<String, Object> data) throws IOException {
        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return mapper.readValue(responseBody, Map.class);
        }
    }

    /**
     * Make a GET request to the REST API.
     *
     * @param endpoint the API endpoint path (e.g., "/users/123")
     * @return the response as a Map containing the JSON response
     * @throws IOException if the HTTP request fails or response cannot be parsed
     */
    public static Map<String, Object> get(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return mapper.readValue(responseBody, Map.class);
        }
    }

    /**
     * Make a PUT request to the REST API.
     *
     * @param endpoint the API endpoint path (e.g., "/users/password")
     * @param data     the request body data as a Map
     * @return the response as a Map containing the JSON response
     * @throws IOException if the HTTP request fails or response cannot be parsed
     */
    public static Map<String, Object> put(String endpoint, Map<String, Object> data) throws IOException {
        String json = mapper.writeValueAsString(data);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .put(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return mapper.readValue(responseBody, Map.class);
        }
    }

    /**
     * Make a DELETE request to the REST API.
     *
     * @param endpoint the API endpoint path (e.g., "/customers/bookings/123")
     * @return the response as a Map containing the JSON response
     * @throws IOException if the HTTP request fails or response cannot be parsed
     */
    public static Map<String, Object> delete(String endpoint) throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + endpoint)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body().string();
            return mapper.readValue(responseBody, Map.class);
        }
    }
}
