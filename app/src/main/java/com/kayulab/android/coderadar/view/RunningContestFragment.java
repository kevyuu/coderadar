package com.kayulab.android.coderadar.view;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.kayulab.android.coderadar.data.ContestContract;
import com.kayulab.android.coderadar.model.OnlineJudge;


/**
 * A simple {@link Fragment} subclass.
 */
public class RunningContestFragment extends ContestListFragment {


    public RunningContestFragment() {
        super();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("DEBUG","RunningContestFragment.onCreateLoader()");

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(ContestFragment.FILTER_PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);

        String whereClause = ContestContract.ContestEntry.COLUMN_START_TIME+" < ? AND " +
                ContestContract.ContestEntry.COLUMN_END_TIME+" > ?";
        whereClause += (" AND " + ContestContract.ContestEntry.COLUMN_SOURCE+" IN (");

        for (int i=0;i< OnlineJudge.OJ_NUMBER;i++) {
            boolean isShown = sharedPref.getBoolean(OnlineJudge.OJ_NAME[i],true);
            if (isShown) {
                whereClause += ("'"+OnlineJudge.OJ_NAME[i] + "', ");
            }
        }
        whereClause = whereClause.substring(0,whereClause.length()-2);
        whereClause += ")";

        String[] whereArgs = {
                Long.toString(System.currentTimeMillis()),
                Long.toString(System.currentTimeMillis())
        };

        return new CursorLoader(getActivity(),
                ContestContract.ContestEntry.CONTENT_URI,
                CONTEST_SUMMARY_COLUMNS,
                whereClause,
                whereArgs,
                ContestContract.ContestEntry.COLUMN_START_TIME+" ASC");
    }
}
