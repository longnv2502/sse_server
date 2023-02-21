package com.saltlux.sseclientjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Message {
    private ConversationInfo conversationInfo;
    private MessageData data;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageData {
        private int index;
        private String text;
    }
}
