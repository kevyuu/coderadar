package com.kayulab.android.coderadar.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.kayulab.android.coderadar.model.ContestSource;
import com.kayulab.android.coderadar.model.ContestStatus;

import java.util.Calendar;
import java.util.EnumSet;

/**
 * Created by kevinyu on 5/19/15.
 */
public class ContestContract {

    public static final String CONTENT_AUTHORITY = "com.kayulab.android.coderadar";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CONTEST = "contest";

    public static final class ContestEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTEST).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTEST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CONTEST;

        public static final String TABLE_NAME = "contest";

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_START_TIME = "start_time";
        public static final String COLUMN_END_TIME = "end_time";
        public static final String COLUMN_SOURCE = "source";

        public static Uri buildContestUriWithId(long id) {
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        public static Uri buildContestWithSource(String source) {
            return CONTENT_URI.buildUpon().appendPath(source).build();
        }

        public static String getSourceFromUri(Uri uri) {
            String source = uri.getLastPathSegment();
            return source;
        }

        public static int getIDFromUri(Uri uri) {
            String id = uri.getLastPathSegment();
            return Integer.parseInt(id);
        }

        public static String buildWhereClause(
                ContestStatus contestStatus,
                EnumSet<ContestSource> contestSources) {
            String whereClause = "";
            if (contestStatus == ContestStatus.RUNNING) {
                whereClause = ContestContract.ContestEntry.COLUMN_START_TIME+" < "+
                        System.currentTimeMillis()+" AND " +
                        ContestContract.ContestEntry.COLUMN_END_TIME+" > "+System.currentTimeMillis();
            } else if (contestStatus == ContestStatus.UPCOMING) {
                whereClause = ContestContract.ContestEntry.COLUMN_START_TIME + " > "
                        + Long.toString(System.currentTimeMillis());
            } else if (contestStatus == ContestStatus.TODAY) {
                Log.d("DEBUG",Long.toString(System.currentTimeMillis()));
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                long startOfTodayMillis = calendar.getTimeInMillis();

                calendar.add(Calendar.DATE, 1);
                long endOfTodayMillis = calendar.getTimeInMillis();

                Log.d("DEBUG",Long.toString(endOfTodayMillis));

                whereClause = ContestContract.ContestEntry.COLUMN_START_TIME+" > "+ startOfTodayMillis
                        +" AND " +
                        ContestContract.ContestEntry.COLUMN_START_TIME+" < "+endOfTodayMillis;
            }


            whereClause += (" AND " + ContestContract.ContestEntry.COLUMN_SOURCE+" IN (");
            for (ContestSource contestSource : contestSources) {
                whereClause += ("'" + contestSource.getName() + "', ");
            }
            whereClause += "'DUMMY')"; //This esure that the IN argument did not empty
                                        // ohterwise the query will be invalid when ContestSource empty

            return whereClause;
        }

    }
}
