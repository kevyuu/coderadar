package com.kayulab.android.coderadar.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;

import com.kayulab.android.coderadar.NotificationService;
import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.sync.SyncAdapter;
import com.kayulab.android.coderadar.utility.PreferenceUtil;


public class MainActivity extends FragmentActivity implements ContestListFragment.Callback{


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            // set an exit transition
            getWindow().setEnterTransition(new Slide());
            getWindow().setReenterTransition(new Slide());
            getWindow().setExitTransition(new Fade());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            Fragment fragment = new ContestFragment();
            FragmentManager fragmentManager= getSupportFragmentManager();
            fragmentManager.
                    beginTransaction().
                    replace(R.id.content_frame,fragment).
                    commit();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().setElevation(0);
        }

        SyncAdapter.initializeSyncAdapter(this);

        boolean isReminderOn = PreferenceUtil.isReminderOn(this);

        if (!NotificationService.isAlarmOn(this) && isReminderOn) {
            NotificationService.startAlarm(this);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;
        }

        return false;
    }

    @Override
    public void onItemSelected(Uri contestUri,ImageView logo) {
        Intent intent = new Intent(this, ContestDetailActivity.class);
        intent.setData(contestUri);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, logo, "logo");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    public void onFilterChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ContestFragment) {
            ((ContestFragment)fragment).onFilterChanged();
        }
    }
}
