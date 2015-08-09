package com.kayulab.android.coderadar.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.view.ContestDetailActivity;

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

            rv.setRemoteAdapter(R.id.contest_listView_widget,intent);

            rv.setEmptyView(R.id.contest_listView_widget,R.id.empty_view);

            Intent detailActivityIntent = new Intent(context, ContestDetailActivity.class);
            PendingIntent detailActivityPendingIntent = PendingIntent.getActivity(context,
                    0,detailActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.contest_listView_widget, detailActivityPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i],rv);
        }

        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        final int nWidget = appWidgetIds.length;
        for (int i=0;i<nWidget;i++) {
            WidgetPreferenceUtil.clearContestListWidgetConfiguration(context,appWidgetIds[i]);
        }
        super.onDeleted(context, appWidgetIds);
    }


}
