package com.flipfit.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {
    
    private static final String BASE_URL = "http://localhost:8080";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    public static String get(String endpoint) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(BASE_URL + endpoint);
            request.setHeader("Content-Type", "application/json");
            
            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    
    public static String post(String endpoint, Object body) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(BASE_URL + endpoint);
            request.setHeader("Content-Type", "application/json");
            
            if (body != null) {
                String json = objectMapper.writeValueAsString(body);
                request.setEntity(new StringEntity(json));
            }
            
            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    
    public static String put(String endpoint, Object body) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPut request = new HttpPut(BASE_URL + endpoint);
            request.setHeader("Content-Type", "application/json");
            
            if (body != null) {
                String json = objectMapper.writeValueAsString(body);
                request.setEntity(new StringEntity(json));
            }
            
            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    
    public static String delete(String endpoint) throws Exception {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpDelete request = new HttpDelete(BASE_URL + endpoint);
            request.setHeader("Content-Type", "application/json");
            
            try (CloseableHttpResponse response = client.execute(request)) {
                return EntityUtils.toString(response.getEntity());
            }
        }
    }
    
    public static <T> T parseResponse(String json, Class<T> clazz) throws Exception {
        return objectMapper.readValue(json, clazz);
    }
}
