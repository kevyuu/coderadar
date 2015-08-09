package com.kayulab.android.coderadar.appwidget;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.RemoteViews;

import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.model.ContestListOption;
import com.kayulab.android.coderadar.model.ContestSource;
import com.kayulab.android.coderadar.model.ContestStatus;
import com.kayulab.android.coderadar.view.ContestDetailActivity;

import java.util.EnumSet;

public class ContestListWidgetConfigureActivity extends Activity {

    private int mAppWidgetId = 0;
    private RadioGroup mRadioGroup;
    private ContestListOption mContestListOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContestListOption = new ContestListOption(
                ContestStatus.RUNNING, EnumSet.allOf(ContestSource.class));

        setContentView(R.layout.activity_contest_list_widget_configure);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mRadioGroup = (RadioGroup) findViewById(R.id.configure_radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int radioButtonId) {
                switch (radioButtonId){
                    case R.id.configure_radio_running :
                        mContestListOption.setStatus(ContestStatus.RUNNING);
                        break;
                    case R.id.configure_radio_today :
                        mContestListOption.setStatus(ContestStatus.TODAY);
                        break;
                    case R.id.configure_radio_upcoming :
                        mContestListOption.setStatus(ContestStatus.UPCOMING);
                        break;
                }
            }
        });

    }


    private void startWidgetService() {
        Intent intent = new Intent(this, ContestListWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        RemoteViews rv = new RemoteViews(this.getPackageName(), R.layout.contest_list_widget);

        rv.setRemoteAdapter(R.id.contest_listView_widget,intent);

        rv.setEmptyView(R.id.contest_listView_widget,R.id.empty_view);

        Intent detailActivityIntent = new Intent(this, ContestDetailActivity.class);
        PendingIntent detailActivityPendingIntent = PendingIntent.getActivity(this,
                0,detailActivityIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        rv.setPendingIntentTemplate(R.id.contest_listView_widget, detailActivityPendingIntent);

        AppWidgetManager.getInstance(this).updateAppWidget(mAppWidgetId, rv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contest_list_widget_configure, menu);
        return true;
    }

    public void onOkButtonClicked(View view) {
        WidgetPreferenceUtil.setContestListWidgetOption(this,mAppWidgetId,mContestListOption);
        startWidgetService();
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,mAppWidgetId);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

    public void onCancelButtonClicked(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void onContestSourceCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.configure_codechef_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.CODECHEF,checked);
                break;
            case R.id.configure_codeforces_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.CODEFORCES,checked);
                break;
            case R.id.configure_hackerrank_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.HACKERRANK,checked);
                break;
            case R.id.configure_hackerearth_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.HACKEREARTH,checked);
                break;
            case R.id.configure_topcoder_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.TOPCODER,checked);
                break;
            case R.id.configure_urioj_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.URIOJ,checked);
                break;
            case R.id.configure_unknown_checkbox:
                mContestListOption.setContestSourceVisibility(
                        ContestSource.UNKNOWN,checked);
                break;
        }
    }

}
