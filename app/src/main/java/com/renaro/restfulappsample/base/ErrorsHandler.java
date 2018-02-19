package com.renaro.restfulappsample.base;

import android.os.Handler;
import android.os.Looper;

import com.renaro.restfulappsample.profile.dao.AppProfileDAO;
import com.renaro.restfulappsample.votes.view.VotingActivityView;

/**
 * Created by renarosantos1 on 18/02/18.
 */

public class ErrorsHandler implements AppProfileDAO.FetchProfilesError {

    private final VotingActivityView mView;

    public ErrorsHandler(VotingActivityView view) {
        mView = view;
    }

    @Override
    public void onServerError() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        mView.showServerError("Seems our server is having a problem, try later");
                    }
                }
        );
    }

    @Override
    public void onInternetConnectionError() {
        new Handler(Looper.getMainLooper()).post(
                new Runnable() {
                    @Override
                    public void run() {
                        mView.showInternetConnectionError("There is a problem with the internet");
                    }
                }
        );
    }
}
