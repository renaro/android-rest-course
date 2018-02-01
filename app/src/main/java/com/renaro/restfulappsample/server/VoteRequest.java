package com.renaro.restfulappsample.server;

/**
 * Created by renarosantos1 on 28/01/18.
 */

public class VoteRequest {

    public int user_id;
    public int voted_id;
    public boolean vote;

    public VoteRequest(final int user_id, final int voted_id, final boolean vote) {
        this.user_id = user_id;
        this.voted_id = voted_id;
        this.vote = vote;
    }

}
