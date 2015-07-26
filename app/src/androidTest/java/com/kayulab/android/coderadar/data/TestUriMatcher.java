package com.kayulab.android.coderadar.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by kevinyu on 5/20/15.
 */
public class TestUriMatcher extends AndroidTestCase {
    private static final long TEST_CONTEST_ID = 1L;
    private static final String TEST_CONTEST_SOURCE = "codeforce";

    private static final Uri CONTEST_URI = ContestContract.ContestEntry.CONTENT_URI;
    private static final Uri CONTEST_WITH_SOURCE_URI = ContestContract.ContestEntry.
            buildContestWithSource(TEST_CONTEST_SOURCE);
    private static final Uri CONTEST_WITH_ID_URI = ContestContract.ContestEntry.
            buildContestUriWithId(TEST_CONTEST_ID);

    public void testUriMatcher() {
        UriMatcher testMatcher = ContestProvider.buildUriMatcher();

        assertEquals("Error : The CONTEST_URI was match incorrectly. ",
                testMatcher.match(CONTEST_URI),ContestProvider.CONTEST);
        assertEquals("Error : The CONTEST_WITH_SOURCE_URI was match incorrectly.",
                testMatcher.match(CONTEST_WITH_SOURCE_URI),ContestProvider.CONTEST_WITH_SOURCE);
        assertEquals("Error : The CONTEST_WITH_ID_URI was match incorrectly.",
                testMatcher.match(CONTEST_WITH_ID_URI),ContestProvider.CONTEST_WITH_ID);
    }
}
