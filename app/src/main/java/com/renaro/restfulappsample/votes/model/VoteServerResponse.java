package com.renaro.restfulappsample.votes.model;

/**
 * Created by Renaro Santos on 25/05/17.
 */

public class VoteServerResponse {

    private final boolean match;
    private final String chat_id;
    private final String message;

    public VoteServerResponse(final boolean match, final String chat_id, final String message) {
        this.match = match;
        this.chat_id = chat_id;
        this.message = message;
    }

    public boolean isMatch() {
        return match;
    }

    public String chatId() {
        return chat_id;
    }

    public String getMessage() {
        return message;
    }
}
