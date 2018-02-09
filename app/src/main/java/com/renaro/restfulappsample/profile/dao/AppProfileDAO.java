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

    /* {
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
        JSONObject root = new JSONObject();
        try {
            root.put("name", "John");
            root.put("age", 34);
            root.put("height", 5.7);
            root.put("single", true);
            String[] movies = new String[]{"Star Wars", "Harry Potter"};
            root.put("favorite_movies", new JSONArray(movies));
            JSONObject address = new JSONObject();
            address.put("street", "Ocean Av, Miami");
            address.put("number", "55");
            root.put("address", address);
            root.put("car", null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return root.toString();
    }

    private void printJsonObject(final String jsonString) {
        try {
            JSONObject root = new JSONObject(jsonString);
            String name = root.getString("name");
            int age = root.getInt("age");
            double height = root.getDouble("height");
            boolean single = root.getBoolean("single");
            JSONArray moviesArrays = root.getJSONArray("favorite_movies");
            String[] movies = new String[moviesArrays.length()];
            for(int i = 0 ; i< moviesArrays.length() ; i++){
                movies[i] = moviesArrays.getString(i);
            }
            JSONObject addressObject = root.getJSONObject("address");
            Log.d("json", "Name ="+name+" age ="+age+" favorite movie="+movies[0]+" address = "+addressObject.getString("street"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
