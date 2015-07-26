package com.kayulab.android.coderadar.model;

import com.kayulab.android.coderadar.R;

import java.util.ArrayList;

/**
 * Created by kevinyu on 5/15/15.
 */
public class OJManager {

    private static OJManager sInstance;
    private ArrayList<OnlineJudge> mOnlineJudges;

    private OJManager(){
        mOnlineJudges = new ArrayList<>();
        mOnlineJudges.add(new OnlineJudge("Hackerrank", R.drawable.hackerrank_logo,
                R.drawable.hackerrank_cover));
        mOnlineJudges.add(new OnlineJudge("Hackerearth", R.drawable.hackerearth_logo,
                R.drawable.hackerearth_cover));
        mOnlineJudges.add(new OnlineJudge("Topcoder",R.drawable.topcoder_logo,
                R.drawable.topcoder_cover));
        mOnlineJudges.add(new OnlineJudge("Codechef",R.drawable.codechef_logo,
                R.drawable.codechef_cover));
    }

    public static OJManager getInstance() {
        if (sInstance == null) {
            sInstance = new OJManager();
        }
        return sInstance;
    }

    public ArrayList<OnlineJudge> getOnlineJudges() {
        return mOnlineJudges;
    }

}
