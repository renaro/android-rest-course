package com.renaro.restfulappsample.server;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by renarosantos on 21/02/17.
 */

public interface BackendServer {

    @GET("profiles/")
    Call<FetchProfileResponse> fetchProfiles();
}
