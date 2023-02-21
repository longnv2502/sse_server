package com.saltlux.sseclientjava.model;


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
    private String userId;
    private String talkbotId;
    private String conversationId;
    private SentAtType sentAt;

    public  enum SentAtType {
        USER,
        BOT
    }
}
