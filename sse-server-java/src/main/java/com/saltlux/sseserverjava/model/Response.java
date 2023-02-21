package com.saltlux.sseserverjava.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Response {

    private BotResponseType type;
    private String message;

   /**
     * Define Bot response type
     */
    public enum BotResponseType {
        TEXT,
        FORM,
        SMART_CARD,
        MOVE_STEP,
        CALL_SERVICE,
        /**
         * Allow trigger full script
         */
        EXECUTE_SCRIPT,
        NOTHING
    }
}
