package com.kayulab.android.coderadar.utility;

import android.test.AndroidTestCase;

import com.kayulab.android.coderadar.model.Contest;
import com.kayulab.android.coderadar.model.OnlineJudge;

import java.util.ArrayList;

/**
 * Created by kevinyu on 5/23/15.
 */
public class TestContestParser extends AndroidTestCase {

    private static String TEST_RSS = "<rss version=\"2.0\">\n" +
            "  <channel>\n" +
            "    <title>Events</title>\n" +
            "    <description>Events</description>\n" +
            "    <link>https://www.hackerrank.com</link>\n" +
            "    <item>\n" +
            "      <title>Codechef - January Cook-Off 2016</title>\n" +
            "      <description></description>\n" +
            "      <url>http://www.codechef.com/COOK65</url>\n" +
            "      <startTime>2016-01-24 16:00:00 UTC</startTime>\n" +
            "      <endTime>2016-01-24 18:30:00 UTC</endTime>\n" +
            "    </item>\n" +
            "    <item>\n" +
            "      <title>Codechef - January Challenge 2016</title>\n" +
            "      <description></description>\n" +
            "      <url>http://www.codechef.com/JAN16</url>\n" +
            "      <startTime>2016-01-01 09:30:00 UTC</startTime>\n" +
            "      <endTime>2016-01-11 09:30:00 UTC</endTime>\n" +
            "    </item>\n" +
            "   </channel>\n"+
            "   </rss>";

    public void testParse() {
        ContestParser parser = new ContestParser();

        ArrayList<Contest> parsedContests =  parser.parse(TEST_RSS);

        ArrayList<Contest> expectedContests = new ArrayList<>();

        Contest expectedContest1 = (new Contest()).title("Codechef - January Cook-Off 2016").
                description("").
                URL("http://www.codechef.com/COOK65").
                source(OnlineJudge.CODECHEF).
                startDate(1453651200000L).
                finishDate(1453660200000L);

        Contest expectedContest2 = (new Contest()).title("Codechef - January Challenge 2016").
                description("").
                URL("http://www.codechef.com/JAN16").
                source(OnlineJudge.CODECHEF).
                startDate(1451640600000L).
                finishDate(1452504600000L);

        expectedContests.add(expectedContest1);
        expectedContests.add(expectedContest2);

        assertEquals("Error : Contest does not parse correctly. There is difference with expected Contests",
                expectedContests,parsedContests);

    }

}
