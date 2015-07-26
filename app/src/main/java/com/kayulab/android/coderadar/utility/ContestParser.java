package com.kayulab.android.coderadar.utility;

import com.kayulab.android.coderadar.model.Contest;
import com.kayulab.android.coderadar.model.OnlineJudge;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by kevinyu on 5/23/15.
 */
public class ContestParser {

    public ArrayList<Contest> parse (String rssString) {
        ArrayList<Contest> contests = new ArrayList<>();

        Document doc = Jsoup.parseBodyFragment(rssString);

        Element channel = doc.getElementsByTag("channel").get(0);

        Elements contestsRSS = channel.getElementsByTag("item");

        for (Element contestRSS : contestsRSS) {
            String title = contestRSS.getElementsByTag("title").get(0).text();
            String description = contestRSS.getElementsByTag("description").get(0).text();
            String url = contestRSS.getElementsByTag("url").get(0).text();
            String startDate = contestRSS.getElementsByTag("startTime").get(0).text();
            String endDate = contestRSS.getElementsByTag("endTime").get(0).text();

            String source = getSourceFromTitleOrURL(title,url);

            Contest contest = (new Contest()).title(title).
                    description(description).
                    URL(url).
                    source(source).
                    startDate(getEpochFromDate(startDate)).
                    finishDate(getEpochFromDate(endDate));

            contests.add(contest);
        }

        return contests;
    }


    private static String getSourceFromTitleOrURL(String title,String url) {
        if (title.indexOf("Codeforces")!=-1) return OnlineJudge.CODEFORCES;
        else if (title.indexOf("Topcoder")!=-1) return OnlineJudge.TOPCODER;
        else if (title.indexOf("Codechef")!=-1) return OnlineJudge.CODECHEF;
        else if (title.indexOf("URIOJ")!=-1) return OnlineJudge.URIOJ;
        else if (title.indexOf("Hackerearth")!=-1) return OnlineJudge.HACKEREARTH;
        else {
            if (url.indexOf("hackerrank")!=-1) return OnlineJudge.HACKERRANK;
            else return OnlineJudge.UNKNOWN;
        }
    }

    private static Long getEpochFromDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
        try {
            Date date = dateFormat.parse(dateString);
            return date.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }
}
