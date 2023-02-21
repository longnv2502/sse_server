package com.saltlux.sseclientjava.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ConversationResponse {

    @JsonProperty("replies")
    private List<Response> replies;

    private String type; // Type of message CM, MM, BM
    private String serverId;
    private String botId;
    private String conversationId;
    private String irqaFeedbackId;

    private String botName;
    private String botAvatarUrl;
    private String botAvatarUrl32;
    private String botAvatarUrl64;
    private Boolean showLastMessage;
    private Boolean enableIrqaFirst;
    private String ttsUrl;
    private String sttLang;

}
