package com.kayulab.android.coderadar.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.kayulab.android.coderadar.R;

/**
 * Created by kevinyu on 8/4/15.
 */
public class ContestListWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("DEBUG","ContestListWidgetProvider.onUpdate()");
        final int N = appWidgetIds.length;

        for (int i=0;i<N;i++) {
            int appWidgetId = appWidgetIds[i];

            Intent intent = new Intent(context, ContestListWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.contest_list_widget);

            rv.setRemoteAdapter(appWidgetIds[i],R.id.contest_listView_widget,intent);

            rv.setEmptyView(R.id.contest_listView,R.id.empty_view);

            appWidgetManager.updateAppWidget(appWidgetIds[i],rv);
        }

        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }
}
