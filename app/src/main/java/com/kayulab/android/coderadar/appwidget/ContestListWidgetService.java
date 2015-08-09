package com.kayulab.android.coderadar.appwidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.data.ContestContract;
import com.kayulab.android.coderadar.model.Contest;
import com.kayulab.android.coderadar.model.ContestListOption;
import com.kayulab.android.coderadar.model.OnlineJudge;
import com.kayulab.android.coderadar.utility.TimeUtil;

import java.util.ArrayList;

/**
 * Created by kevinyu on 8/4/15.
 */
public class ContestListWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("DEBUG","RemoteViewsFactory.onGetViewFactory");
        return new ContestListRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ContestListRemoteViewsFactory implements RemoteViewsFactory {

        private Context mContext;
        private int mAppWidgetId;
        private ContestListOption mContestListOption;
        private ArrayList<Contest> mContests = new ArrayList<>();
        private ArrayList<Long> mContestId = new ArrayList<>();
        private int mCount;

        protected final String[] CONTEST_SUMMARY_COLUMNS = {
                ContestContract.ContestEntry._ID,
                ContestContract.ContestEntry.COLUMN_TITLE,
                ContestContract.ContestEntry.COLUMN_SOURCE,
                ContestContract.ContestEntry.COLUMN_START_TIME
        };

        static final int COL_CONTEST_ID = 0;
        static final int COL_CONTEST_TITLE = 1;
        static final int COL_CONTEST_SOURCE = 2;
        static final int COL_CONTEST_START_TIME = 3;

        public ContestListRemoteViewsFactory (Context context, Intent intent) {
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {

            mContestListOption = WidgetPreferenceUtil.getContestListWidgetOption(mContext, mAppWidgetId);
            String whereClause = ContestContract.ContestEntry.buildWhereClause(
                    mContestListOption.getStatus(),mContestListOption.getContestSource());

            Log.d("DEBUG",whereClause);

            Cursor cursor = mContext.getContentResolver().query(ContestContract.ContestEntry.CONTENT_URI,
                    CONTEST_SUMMARY_COLUMNS,
                    whereClause,
                    null,
                    ContestContract.ContestEntry.COLUMN_START_TIME+" ASC");

            mCount = 0;

            for (int i=0;i < cursor.getCount(); i++) {
                cursor.moveToNext();
                String source = cursor.getString(COL_CONTEST_SOURCE);

                String title = cursor.getString(COL_CONTEST_TITLE);

                Long startTime = cursor.getLong(COL_CONTEST_START_TIME);

                Long id = cursor.getLong(COL_CONTEST_ID);

                Contest contest = new Contest();
                contest.source(source).title(title).startDate(startTime);

                mContests.add(contest);
                mContestId.add(id);

                mCount += 1;
            }

            cursor.close();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            mContests.clear();
        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public RemoteViews getViewAt(int position) {

            Contest contest = mContests.get(position);

            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.contest_list_item);
            rv.setTextViewText(R.id.contestName_textView, contest.getTitle());
            rv.setTextViewText(R.id.onlineJudge_textView, contest.getSource());

            Long timeLeft = contest.getStartDate() - System.currentTimeMillis();
            rv.setTextViewText(R.id.timeBeforeContest_textView, TimeUtil.getShortReadableDurationFromMillis(timeLeft));
            rv.setImageViewResource(R.id.onlineJudge_image,OnlineJudge.getIcon(contest.getSource()));

            Intent fillInIntent = new Intent();
            fillInIntent.setData(ContestContract.ContestEntry.buildContestUriWithId(mContestId.get(position)));
            rv.setOnClickFillInIntent(R.id.contest_item_container,fillInIntent);

            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.loading_contest_list);
            return rv;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
