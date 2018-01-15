package com.renaro.restfulappsample.profile.dao;

import com.renaro.restfulappsample.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by renarosantos on 21/02/17.
 */

public class AppProfileDAO extends ProfileDAO {

    public static final int NUMBER_OF_USERS = 6;
    private static final int FAKE_MATCH_ID = 3;

    public AppProfileDAO() {

    }

    @Override
    public List<UserProfile> fetchProfiles() {

        ArrayList<UserProfile> userProfiles = new ArrayList<>();
        String imageUrl = "https://www.gravatar.com/wavatar/%d?s=200";
        for (int i = 1; i <= NUMBER_OF_USERS; i++) {
            userProfiles.add(new UserProfile(i, String.format(Locale.getDefault(), imageUrl, i), "Person " + i, 21 + i));
        }

        return userProfiles;
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
