package com.renaro.restfulappsample.server;

import com.renaro.restfulappsample.votes.model.VoteServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by renarosantos on 21/02/17.
 */

public interface BackendServer {

    @GET("profiles/")
    Call<FetchProfileResponse> fetchProfiles();

    @POST("vote/")
    Call<VoteServerResponse> voteProfile(@Body VoteRequest request);
}
