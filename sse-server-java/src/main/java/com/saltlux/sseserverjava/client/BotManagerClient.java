package com.saltlux.sseserverjava.client;

import com.saltlux.sseserverjava.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BotManagerClient {
    private final RestTemplate restTemplate;

    public BotManagerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Value("${bot-manager.server}")
    private String BASE_URL;
    private static final String PATH_URL = "/api/chat";

    private static final String START_CONVERSATION_END_POINT = "/{talkbotId}/startConversation/{userId}";

    public String startConversation(String talkbotId, String userId){
        String endpoint = BASE_URL + PATH_URL + START_CONVERSATION_END_POINT
                .replace("{talkbotId}", talkbotId)
                .replace("{userId}", userId);
        String json = HttpUtils.sendGet(this.restTemplate, endpoint);
        return json;
    }
}
