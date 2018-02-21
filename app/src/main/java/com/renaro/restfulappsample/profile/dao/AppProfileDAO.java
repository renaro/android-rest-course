package com.renaro.restfulappsample.profile.dao;

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
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by renarosantos on 21/02/17.
 */

public class AppProfileDAO extends ProfileDAO {

    public static final int STATUS_OK = 200;
    public static final int STATUS_SERVER_ERROR = 500;
    public static final int TIMEOUT = 15;
    private static final int USER_ID = 131;
    private final BackendServer mService;
    private FetchProfilesError fetchProfilesError;

    public AppProfileDAO(FetchProfilesError profilesError) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mService = retrofit.create(BackendServer.class);
        fetchProfilesError = profilesError;


    }


    /*
    * DAO  notify the ErrorsHandler class if there was any error, and return null.
    * If there was any error, my ErrorsHandler class will notify my VIEW
    *
    * DAO -> ErrorsHandler -> View
    * */
    @Override
    public List<UserProfile> fetchProfiles() {
        try {
            Response<FetchProfileResponse> response = mService.fetchProfiles().execute();
            if (response.code() == STATUS_OK && response.body() != null) {
                return response.body().getProfiles();
            } else {
                switch (response.code()) {
                    case STATUS_SERVER_ERROR:
                        fetchProfilesError.onServerError();
                        return null;
                    default:
                        return null;
                }
            }
        } catch (IOException e) {
            fetchProfilesError.onInternetConnectionError();
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

    public interface FetchProfilesError {
        void onServerError();

        void onInternetConnectionError();
    }

}
