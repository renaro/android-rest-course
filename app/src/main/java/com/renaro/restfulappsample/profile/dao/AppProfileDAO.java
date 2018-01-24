package com.renaro.restfulappsample.profile.dao;

import android.util.Log;

import com.renaro.restfulappsample.profile.model.UserProfile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.renaro.restfulappsample.BuildConfig.SERVER_BASE_URL;


/**
 * Created by renarosantos on 21/02/17.
 */

public class AppProfileDAO extends ProfileDAO {

    public static final int NUMBER_OF_USERS = 6;
    public static final int STATUS_OK = 200;
    private static final int FAKE_MATCH_ID = 3;

    public AppProfileDAO() {

    }

    @Override
    public List<UserProfile> fetchProfiles() {
        ArrayList<UserProfile> result = new ArrayList<>();
        HttpURLConnection connection = null;
        try {
            /*defining the URL that we want to get our profiles and opening a connection.
              The default method of HttpURLConnection is GET, which is the one we want in this case.
              So no need to set up the method then.
            SERVER_BASE_URL =https://demo1046700.mockable.io/tinderlikeapp/
            The profiles resources are here:
            https://demo1046700.mockable.io/tinderlikeapp/profiles/
             */
            URL url = new URL(SERVER_BASE_URL.concat("profiles/"));
            connection = (HttpURLConnection) url.openConnection();
            //getting the response of the request
            if (connection.getResponseCode() == STATUS_OK) {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                result.addAll(readInput(in));
            } else {
                Log.e("ERROR", "The response code is : " + connection.getResponseCode());
            }
        } catch (JSONException | IOException e) {
            Log.e("ERROR", "There was an error in the request");
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    private ArrayList<UserProfile> readInput(final InputStream input) throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input));

        String inputLine;
        StringBuffer response = new StringBuffer();
        //reading the response
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        //getting the response into a String
        String jsonResponse = response.toString();
        System.out.println(jsonResponse);
        return convertJsonToUserList(jsonResponse);
    }

    private ArrayList<UserProfile> convertJsonToUserList(final String jsonResponse) throws JSONException {
        ArrayList<UserProfile> result = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONArray profilesJson = jsonObject.getJSONArray("profiles");
        for (int i = 0; i < profilesJson.length(); i++) {
            final int id = profilesJson.getJSONObject(i).getInt("id");
            final String name = profilesJson.getJSONObject(i).getString("name");
            final String age = profilesJson.getJSONObject(i).getString("age");
            final String cover = profilesJson.getJSONObject(i).getString("cover");
            result.add(new UserProfile(id, cover, name, Integer.parseInt(age)));
        }
        return result;
    }

    @Override
    public int fetchRemainingVotes() {
        return 3;
    }

    @Override
    public boolean voteProfile(final UserProfile profile, final boolean vote) {
        return profile.getId() == FAKE_MATCH_ID && vote;
    }
}
