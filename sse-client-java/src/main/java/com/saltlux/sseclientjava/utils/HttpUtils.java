package com.saltlux.sseclientjava.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public final class HttpUtils {

    private static HttpHeaders createHeaders(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if(!StringUtils.isNullOrEmpty(token))
            headers.setBearerAuth(token);

        return headers;
    }

    private static HttpHeaders createHeaders(){
        return createHeaders(null);
    }

    public static String sendPost(RestTemplate restTemplate, String endpoint, String body) {
        HttpEntity<String> request = new HttpEntity<>(body, createHeaders());
        ResponseEntity<String> resp = restTemplate.postForEntity(endpoint, request, String.class);
        return resp.getBody();
    }

    public static String sendPost(RestTemplate restTemplate, String endpoint, String body,  String token) {
        HttpEntity<String> request = new HttpEntity<>(body, createHeaders(token));
        ResponseEntity<String> resp = restTemplate.postForEntity(endpoint, request, String.class);
        return resp.getBody();
    }

    public static String sendPost(RestTemplate restTemplate, String endpoint) {
        return sendPost(restTemplate, endpoint, null);
    }

    public static String sendGet(RestTemplate restTemplate, String endpoint, String token) {
        HttpEntity<String> request = new HttpEntity<>(createHeaders(token));
        ResponseEntity<String> resp = restTemplate.exchange(endpoint, HttpMethod.GET, request, String.class);
        return resp.getBody();
    }

    public static String sendGet(RestTemplate restTemplate, String endpoint) {
        return sendGet(restTemplate, endpoint);
    }
}
