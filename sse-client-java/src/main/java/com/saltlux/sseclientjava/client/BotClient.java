package com.saltlux.sseclientjava.client;

import com.saltlux.sseclientjava.model.ConversationResponse;
import com.saltlux.sseclientjava.utils.HttpUtils;
import com.saltlux.sseclientjava.utils.JacksonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class BotClient {

    private final RestTemplate restTemplate;

    @Value("${talkbot.api.server}")
    private String BASE_URL;
    private final String PATH_URL = "/chat";
    private final String START_CONVERSATION_END_POINT = "/{talkbotId}/startConversation";
    private final String STOP_CONVERSATION_END_POINT = "/{talkbotId}/stopConversation/{conversationId}";
    private final String SEND_MESSAGE_END_POINT = "/{talkbotId}/{conversationId}/{index}";

    @Value("${api.token}")
    private String token;

    public BotClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ConversationResponse startConversation(String talkbotId){
        String endpoint = BASE_URL + PATH_URL + START_CONVERSATION_END_POINT.replace("{talkbotId}", talkbotId);
        String json = HttpUtils.sendGet(this.restTemplate, endpoint,  token);
        return JacksonUtils.getObject(json, ConversationResponse.class);
    }

    public void stopConversation(String talkbotId, String conversationId){
        String endpoint = BASE_URL + PATH_URL + STOP_CONVERSATION_END_POINT.replace("{talkbotId}", talkbotId).replace("{conversationId}", conversationId);
        HttpUtils.sendGet(this.restTemplate, endpoint,  token);
    }

    public ConversationResponse sendMessage(String talkbotId, String conversationId, int index, String text){
        String endpoint = BASE_URL + PATH_URL + SEND_MESSAGE_END_POINT
                .replace("{talkbotId}", talkbotId)
                .replace("{conversationId}", conversationId)
                .replace("{index}", String.valueOf(index));
        String json = HttpUtils.sendPost(this.restTemplate, endpoint, text,  token);
        return JacksonUtils.getObject(json, ConversationResponse.class);
    }

}
