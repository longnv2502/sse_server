package com.saltlux.sseserverjava.controller;

import com.saltlux.sseserverjava.model.ConversationInfo;
import com.saltlux.sseserverjava.model.Message;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/sse-server")
public interface IConversationSseController {

    @GetMapping("/{talkbotId}/startConversation/{userId}")
    ResponseEntity<?> startConversation(@PathVariable String talkbotId, @PathVariable String userId);

    @GetMapping(value = "/{talkbotId}/subscribe/{userId}/{conversationId}", consumes = MediaType.ALL_VALUE)
    SseEmitter subscribe(@PathVariable String talkbotId,
                         @PathVariable String userId,
                         @PathVariable String conversationId,
                         @RequestParam ConversationInfo.SentAtType sentAt);

    @PostMapping("/send")
    void sendMessage(@RequestBody Message message);
}
