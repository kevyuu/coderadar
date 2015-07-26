package com.kayulab.android.coderadar;

import android.app.Application;
import android.content.Context;

/**
 * Created by kevinyu on 6/21/15.
 * This class is created to provide Context object for every class.
 * For example to get resources in string.xml. We can call App.getContext().getResources() and so on.
 */
public class CRApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }

    public static Context getContext() {
        return mContext;
    }
}
