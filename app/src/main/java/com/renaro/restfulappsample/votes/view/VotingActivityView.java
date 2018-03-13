package com.renaro.restfulappsample.votes.view;


import android.support.annotation.NonNull;

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

    void showOutOfVotes();

    void showEmptyList();

    void onProfileRemoved();

    void showServerError(String message);

    void showInternetConnectionError(String message);

}
