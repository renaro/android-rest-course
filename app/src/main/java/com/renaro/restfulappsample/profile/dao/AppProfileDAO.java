package com.renaro.restfulappsample.profile.dao;

import android.util.Log;

import com.renaro.restfulappsample.BuildConfig;
import com.renaro.restfulappsample.profile.model.UserProfile;
import com.renaro.restfulappsample.server.BackendServer;
import com.renaro.restfulappsample.server.FetchProfileResponse;
import com.renaro.restfulappsample.server.VoteRequest;
import com.renaro.restfulappsample.votes.model.VoteServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        String jsonString = createJsonString();
        Log.d("JSON ", jsonString);
        printJsonObject(jsonString);
    }

    private void printJsonObject(final String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            String name = root.getString("name");
            int age = root.getInt("age");
            JSONArray jsonArray = root.getJSONArray("favorite_movies");
            String[] movies = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                movies[i] = jsonArray.getString(i);
            }
            String streetName = root.getJSONObject("address").getString("street");
            Log.d("JSON","Name =" + name+ ", age : " + age + ", Favorite movie =" + movies[0] + ", street name =" + streetName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
      {
        "name": "John",
        "age": 34,
        "height": 5.7,
        "single": true,
        "favorite_movies": ["Star Wars", "Harry Potter"],
        "address": {
            "street": "Ocean Av, Miami",
            "number": "55"
            },
        "car": null
      }
    */
    private String createJsonString() {
        JSONObject jsonRoot = new JSONObject();
        try {
            jsonRoot.put("name", "John");
            jsonRoot.put("age", 34);
            String[] movies = new String[]{"Star Wars", "Harry Potter"};
            jsonRoot.put("favorite_movies", new JSONArray(movies));
            JSONObject address = new JSONObject();
            address.put("street", "Ocean Av, Miami");
            address.put("number", "55");
            jsonRoot.put("address", address);
            jsonRoot.put("car", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonRoot.toString();
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
