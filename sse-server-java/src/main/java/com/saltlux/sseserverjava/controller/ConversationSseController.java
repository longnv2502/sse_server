package com.saltlux.sseserverjava.controller;

import com.saltlux.sseserverjava.client.BotManagerClient;
import com.saltlux.sseserverjava.model.ConversationInfo;
import com.saltlux.sseserverjava.model.ConversationResponse;
import com.saltlux.sseserverjava.model.Message;
import com.saltlux.sseserverjava.utils.JacksonUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ConversationSseController implements IConversationSseController {
    private static final Logger logger = LoggerFactory.getLogger(ConversationSseController.class);
    public Map<ConversationInfo, SseEmitter> emitters = new HashMap<>();

    private final BotManagerClient botManagerClient;

    public ConversationSseController(BotManagerClient botManagerClient) {
        this.botManagerClient = botManagerClient;
    }

    @Override
    public ResponseEntity<?> startConversation(String talkbotId, String userId) {
        return ResponseEntity.ok(botManagerClient.startConversation(talkbotId, userId));
    }

    @Override
    public SseEmitter subscribe(String talkbotId, String userId, String conversationId, ConversationInfo.SentAtType sentAt) {
        logger.info("Call API /subscribe with talkbotId: {}, userId: {}, conversationId: {}, sentAt: {}", talkbotId, userId, conversationId, sentAt);
        SseEmitter sseEmitter = new SseEmitter(Long.MAX_VALUE);
        sendInitEvent(sseEmitter);
        emitters.put(ConversationInfo.getConversationSubscribe(new ConversationInfo(talkbotId, userId, conversationId, sentAt)), sseEmitter);
        sseEmitter.onCompletion(() -> emitters.remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitters.remove(sseEmitter));
        sseEmitter.onError((e) -> emitters.remove(sseEmitter));
        return sseEmitter;
    }

    @Override
    public void sendMessage(@NotNull Message message) {
        logger.info("Call API /send with message: {}", JacksonUtils.getJson(message));
        ConversationInfo conversationInfo = message.getConversationInfo();
        if (conversationInfo == null) return;
        SseEmitter sseEmitter = emitters.get(conversationInfo);
        if (sseEmitter == null) return;
        try {
            sseEmitter.send(SseEmitter.event().name("latest").data(message.getData()));
        } catch (IOException e) {
            emitters.remove(sseEmitter);
            logger.debug(e.getMessage(), e);
        }
    }

    private void sendInitEvent(SseEmitter sseEmitter) {
        try {
            sseEmitter.send(SseEmitter.event().name("INIT"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
