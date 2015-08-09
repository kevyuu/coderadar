package com.kayulab.android.coderadar.appwidget;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.kayulab.android.coderadar.model.ContestListOption;

/**
 * Created by kevinyu on 8/7/15.
 */
public class WidgetPreferenceUtil {

    private static final String PREFERENCE_FILE_KEY = WidgetPreferenceUtil.class.getName() + "_WIDGET_FILE_KEY";

    public static void setContestListWidgetOption(Context context, int appWidgetId, ContestListOption contestListOption) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String contestListOptionJSON = gson.toJson(contestListOption);
        editor.putString(Integer.toString(appWidgetId),contestListOptionJSON);
        editor.commit();
    }

    public static ContestListOption getContestListWidgetOption(Context context,int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        String contestListJSON = sharedPreferences.getString(Integer.toString(appWidgetId),
                "");

        Gson gson = new Gson();
        ContestListOption contestListOption = gson.fromJson(contestListJSON,ContestListOption.class);
        return contestListOption;
    }

    public static void clearContestListWidgetConfiguration(Context context,int appWidgetId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(Integer.toString(appWidgetId));
        editor.commit();
    }

}
