package com.saltlux.sseclientjava.client;

import com.saltlux.sseclientjava.model.ConversationResponse;
import com.saltlux.sseclientjava.model.Message;
import com.saltlux.sseclientjava.utils.HttpUtils;
import com.saltlux.sseclientjava.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SseClient {
    private final RestTemplate restTemplate;

    @Value("${sse.server}")
    private String BASE_URL;
    private static final String PATH_URL = "/sse-server";
    private final String SEND_END_POINT = "/send";

    public SseClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendMessage(Message message){
        String endpoint = BASE_URL + PATH_URL + SEND_END_POINT;
        HttpUtils.sendPost(this.restTemplate, endpoint, JacksonUtils.getJson(message));
    }
}
