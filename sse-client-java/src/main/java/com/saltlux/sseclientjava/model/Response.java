package com.saltlux.sseclientjava.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


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
