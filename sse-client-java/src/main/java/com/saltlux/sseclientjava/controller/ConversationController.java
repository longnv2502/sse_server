package com.saltlux.sseclientjava.controller;

import com.saltlux.sseclientjava.client.BotClient;
import com.saltlux.sseclientjava.client.SseClient;
import com.saltlux.sseclientjava.model.ConversationInfo;
import com.saltlux.sseclientjava.model.ConversationResponse;
import com.saltlux.sseclientjava.model.Message;
import com.saltlux.sseclientjava.utils.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
public class ConversationController implements IConversationController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

    @Value("${sse.server}")
    private String SSE_SERVER_BASE_URL;
    private static final String PATH_URL = "/sse-server";
    private static final String SUBSCRIBE_END_POINT = "/{talkbotId}/subscribe/{userId}/{conversationId}?sentAt={sentAt}";
    private static final String SEND_END_POINT = "/send";

    private final BotClient botClient;
    private final SseClient sseClient;

    public ConversationController(BotClient botClient, SseClient sseClient) {
        this.botClient = botClient;
        this.sseClient = sseClient;
    }


    @Override
    public ResponseEntity<?> startConversation(String talkbotId, String userId) {
        ConversationResponse cr = botClient.startConversation(talkbotId);
        ConversationInfo conversationInfo = new ConversationInfo(userId, talkbotId, cr.getConversationId(), ConversationInfo.SentAtType.BOT);
        consumeServerSentEvent(conversationInfo);
        return ResponseEntity.ok(cr);
    }

    private void consumeServerSentEvent(ConversationInfo conversationInfo) {
        WebClient client = WebClient.create(SSE_SERVER_BASE_URL + PATH_URL);
        ParameterizedTypeReference<ServerSentEvent<String>> type = new ParameterizedTypeReference<>() {
        };

        String uri = SUBSCRIBE_END_POINT
                .replace("{talkbotId}", conversationInfo.getTalkbotId())
                .replace("{userId}", conversationInfo.getUserId())
                .replace("{conversationId}", conversationInfo.getConversationId())
                .replace("{sentAt}", conversationInfo.getSentAt().toString());

        Flux<ServerSentEvent<String>> eventStream = client.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(type);

        eventStream.subscribe(
                content -> {
                    switch (Objects.requireNonNull(content.event())) {
                        case "latest" -> {
                            Message.MessageData receiveMessageData = JacksonUtils.getObject(content.data(), Message.MessageData.class);
                            ConversationResponse cr = botClient.sendMessage(conversationInfo.getTalkbotId(), conversationInfo.getConversationId(), receiveMessageData.getIndex(), receiveMessageData.getText());
                            Message sendMessage = new Message().setConversationInfo(conversationInfo).setData(new Message.MessageData(receiveMessageData.getIndex() + 1, JacksonUtils.getJson(cr.getReplies())));
                            sseClient.sendMessage(sendMessage);
                        }
                        case "..." -> logger.info(content.data());
                        default -> {
                        }
                    }
                },
                error -> {
                    logger.error("Error receiving SSE: ", error);
                    botClient.stopConversation(conversationInfo.getTalkbotId(), conversationInfo.getConversationId());
                },
                () -> botClient.stopConversation(conversationInfo.getTalkbotId(), conversationInfo.getConversationId()));
    }
}
