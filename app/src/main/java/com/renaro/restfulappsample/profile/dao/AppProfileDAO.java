package com.renaro.restfulappsample.profile.dao;

import com.renaro.restfulappsample.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by renarosantos on 21/02/17.
 */

public class AppProfileDAO extends ProfileDAO {

    private static final int FAKE_MATCH_ID = 3;

    public AppProfileDAO() {

    }

    @Override
    public List<UserProfile> fetchProfiles() {

        ArrayList<UserProfile> userProfiles = new ArrayList<>();
        userProfiles.add(new UserProfile(1,"https://www.gravatar.com/wavatar/1?s=200", "Joana",23 ));
        userProfiles.add(new UserProfile(2,"https://www.gravatar.com/wavatar/2?s=200", "John",31 ));
        userProfiles.add(new UserProfile(3,"https://www.gravatar.com/wavatar/3?s=200", "Louise",32 ));
        userProfiles.add(new UserProfile(4,"https://www.gravatar.com/wavatar/4?s=200", "Mark",33 ));
        userProfiles.add(new UserProfile(5,"https://www.gravatar.com/wavatar/5?s=200", "Anna",39 ));
        userProfiles.add(new UserProfile(6,"https://www.gravatar.com/wavatar/6?s=200", "Roger",26 ));
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
