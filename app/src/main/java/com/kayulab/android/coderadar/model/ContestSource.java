package com.kayulab.android.coderadar.model;

/**
 * Created by kevinyu on 8/8/15.
 */
public enum ContestSource {
    CODEFORCES("Codeforces"),
    TOPCODER("Topcoder"),
    CODECHEF("Codechef"),
    HACKERRANK("Hackerrank"),
    URIOJ("URIOJ"),
    HACKEREARTH("Hackerearth"),
    UNKNOWN("Uknown");

    private String mName;
    ContestSource(String name) {
        mName = name;
    }

    public String getName() {
        return mName;
    }

}
