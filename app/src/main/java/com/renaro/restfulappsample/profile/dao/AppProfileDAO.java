package com.renaro.restfulappsample.profile.dao;

import android.util.Log;

import com.google.gson.Gson;
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

        String jsonString = createJsonString();
        Log.d("JSON", jsonString);
        printJsonObject(jsonString);
    }

    private void printJsonObject(final String jsonString) {
        Gson gson = new Gson();
        Person person = gson.fromJson(jsonString, Person.class);
        Log.d("json", "Name : "+person.address.street);


    }

    private String createJsonString() {
        Gson converter = new Gson();
        Person person = new Person("John", 34, 5.7, true, new String[]{"Harry Potter", "Star Wars"},
                "Ocean Av, Miami", "55b");
        return converter.toJson(person);
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

     /* Sample data we are going to use
    * {
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
    * */

    public class Person {
        private String name;
        private int age;
        private double height;
        private boolean single;
        private String[] favoriteMovies;
        private PersonAddress address;

        public Person(final String name, final int age, final double height,
                      final boolean single, final String[] favoriteMovies, String street, String number) {
            this.name = name;
            this.age = age;
            this.height = height;
            this.single = single;
            this.favoriteMovies = favoriteMovies;
            this.address = new PersonAddress(street, number);

        }


        public class PersonAddress {
            private String street;
            private String number;

            public PersonAddress(final String street, final String number) {
                this.street = street;
                this.number = number;
            }
        }
    }


}
