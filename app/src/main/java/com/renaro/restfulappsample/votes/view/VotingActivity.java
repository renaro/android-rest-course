package com.renaro.restfulappsample.votes.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.renaro.restfulappsample.R;
import com.renaro.restfulappsample.base.BaseActivity;
import com.renaro.restfulappsample.base.ErrorsHandler;
import com.renaro.restfulappsample.profile.bo.ProfileBO;
import com.renaro.restfulappsample.profile.dao.AppProfileDAO;
import com.renaro.restfulappsample.profile.model.UserProfile;
import com.renaro.restfulappsample.task.AppTaskExecutor;
import com.renaro.restfulappsample.votes.presenter.VotingPresenter;

import java.util.List;


public class VotingActivity extends BaseActivity<VotingPresenter>
        implements VotingActivityView {

    private View loading;
    private SwipeFlingAdapterView mSwipeList;
    private ProfileAdapter mAdapter;
    private MatchDialog mMatchDialog;

    @NonNull
    @Override
    protected VotingPresenter createPresenter(@NonNull final Context context) {
        return new VotingPresenter(this, new AppTaskExecutor(this),
                new ProfileBO(new AppProfileDAO(new ErrorsHandler(this))));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);
        final View heartIcon = findViewById(R.id.heart_icon);
        final View brokenHeartIcon = findViewById(R.id.broken_hear_icon);

        loading = findViewById(R.id.loading);
        mSwipeList = findViewById(R.id.swipe_list);
        mSwipeList.setFlingListener(new SwipeListener());
        OnVoteButtonsClicked onVoteButtonsClicked = new OnVoteButtonsClicked();
        heartIcon.setOnClickListener(onVoteButtonsClicked);
        brokenHeartIcon.setOnClickListener(onVoteButtonsClicked);

    }


    @Override
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showProfiles(final List<UserProfile> profiles) {
        UserProfile[] array = new UserProfile[profiles.size()];
        mAdapter = new ProfileAdapter(this, R.layout.profile_card, profiles.toArray(array));
        mSwipeList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showNegativeVote() {
        mSwipeList.getTopCardListener().selectLeft();
    }

    @Override
    public void showPositiveVote() {
        mSwipeList.getTopCardListener().selectRight();
    }

    @Override
    public void showMatch(final UserProfile profile) {
        mMatchDialog = new MatchDialog(this);
        mMatchDialog.setProfile(profile);
        mMatchDialog.show();
    }

    @Override
    public void showOutOfVotes() {
        Toast.makeText(this, "Out of Votes! Wait", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyList() {

    }

    @Override
    public void onProfileRemoved() {
        mAdapter.profileRemoved();
    }

    @Override
    public void showServerError(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showInternetConnectionError(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMatchDialog != null) {
            mMatchDialog.dismiss();
        }
    }

    private class SwipeListener implements SwipeFlingAdapterView.onFlingListener {
        @Override
        public void removeFirstObjectInAdapter() {
            mPresenter.firstCardRemoved();
        }

        @Override
        public void onLeftCardExit(final Object o) {
            mPresenter.onSlideProfileToLeft();
        }

        @Override
        public void onRightCardExit(final Object o) {
            mPresenter.onSlideProfileToRight();
        }

        @Override
        public void onAdapterAboutToEmpty(final int i) {
        }

        @Override
        public void onScroll(final float v) {
        }
    }

    private class OnVoteButtonsClicked implements View.OnClickListener {
        @Override
        public void onClick(final View view) {
            switch (view.getId()) {
                case R.id.heart_icon:
                    mPresenter.onPositiveButtonClicked();
                    break;
                case R.id.broken_hear_icon:
                    mPresenter.onNegativeButtonClicked();
                    break;
            }
        }
    }
}
