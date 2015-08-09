package com.kayulab.android.coderadar.model;

import java.util.EnumSet;

/**
 * Created by kevinyu on 8/7/15.
 */
public class ContestListOption {


    private EnumSet<ContestSource> mVisibleContestSources;

    private ContestStatus mStatus;

    public ContestListOption(ContestStatus status, EnumSet<ContestSource> visibleContestSources) {
        mVisibleContestSources = visibleContestSources;
        mStatus = status;
    }

    public void setContestSourceVisibility(ContestSource contestSource,boolean isVisible) {
        if (isVisible) {
            mVisibleContestSources.add(contestSource);
        } else {
            mVisibleContestSources.remove(contestSource);
        }
    }

    public boolean isContestSourceVisible(ContestSource contestSource) {
        return mVisibleContestSources.contains(contestSource);
    }

    public EnumSet<ContestSource> getContestSource() {
        return mVisibleContestSources;
    }

    public ContestStatus getStatus() {
        return mStatus;
    }

    public void setStatus(ContestStatus status) {
        mStatus = status;
    }

}
