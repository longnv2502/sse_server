package com.saltlux.sseserverjava.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
public class ConversationInfo {
    private String talkbotId;
    private String userId;
    private String conversationId;
    private SentAtType sentAt;

    public enum SentAtType {
        USER,
        BOT
    }

    public static ConversationInfo getConversationSubscribe(ConversationInfo conversationInfo) {
        SentAtType sentAt = conversationInfo.sentAt.equals(SentAtType.BOT) ? SentAtType.USER : SentAtType.BOT;
        return conversationInfo.setSentAt(sentAt);
    }
}
