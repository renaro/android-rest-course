package com.renaro.restfulappsample.votes.view;


import com.renaro.restfulappsample.profile.model.UserProfile;

import java.util.List;

/**
 * Created by renarosantos on 05/02/17.
 */
public interface VotingActivityView {

    void showLoading();

    void hideLoading();

    void showProfiles(List<UserProfile> profiles);

    void showNegativeVote();

    void showPositiveVote();

    void showMatch(UserProfile profile);

    int cardsLeft();

    void showOutOfVotes();
}
