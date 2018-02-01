package com.renaro.restfulappsample.profile.dao;

import android.util.Log;

import com.renaro.restfulappsample.BuildConfig;
import com.renaro.restfulappsample.profile.model.UserProfile;
import com.renaro.restfulappsample.server.BackendServer;
import com.renaro.restfulappsample.server.FetchProfileResponse;
import com.renaro.restfulappsample.server.VoteRequest;
import com.renaro.restfulappsample.votes.model.VoteServerResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by renarosantos on 21/02/17.
 */

public class AppProfileDAO extends ProfileDAO {

    public static final int TIMEOUT = 30;
    private static final int FAKE_MATCH_ID = 3;
    private static final int USER_ID = 131;
    private final BackendServer mService;

    public AppProfileDAO() {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.SERVER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(BackendServer.class);

    }

    @Override
    public List<UserProfile> fetchProfiles() {
        try {
            Response<FetchProfileResponse> response = mService.fetchProfiles().execute();
            if (response.body() != null) {
                return response.body().getProfiles();
            }
        } catch (IOException e) {
            Log.e("ERROR", "Internet Connection", e);
        }
        return new ArrayList<>();
    }

    @Override
    public int fetchRemainingVotes() {
        return 3;
    }

    @Override
    public boolean voteProfile(final UserProfile profile, final boolean vote) {
        boolean isMatch = false;
        try {
            Response<VoteServerResponse> response = mService.voteProfile(new VoteRequest(USER_ID, profile.getId(), vote)).execute();
            VoteServerResponse body = response.body();
            if (body != null) {
                isMatch = body.isMatch();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isMatch;
    }
}
