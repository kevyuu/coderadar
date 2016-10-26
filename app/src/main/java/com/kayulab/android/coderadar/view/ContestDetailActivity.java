package com.kayulab.android.coderadar.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.model.Contest;

public class ContestDetailActivity extends FragmentActivity {

    ViewPager mViewPager;
    Contest contest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // inside your activity (if you did not enable transitions in your theme)
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

            // set an exit transition
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contest_detail);



        if (savedInstanceState == null) {
            Uri contestUri = (Uri) getIntent().getData();
            Fragment fragment = ContestDetailFragment.newInstance(contestUri);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contest_detail_container,fragment)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contest_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }
}
