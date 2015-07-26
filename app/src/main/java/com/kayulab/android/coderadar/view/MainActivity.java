package com.kayulab.android.coderadar.view;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kayulab.android.coderadar.NotificationService;
import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.sync.SyncAdapter;
import com.kayulab.android.coderadar.utility.PreferenceUtil;


public class MainActivity extends FragmentActivity implements ContestListFragment.Callback{

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private static final int NAV_CONTEST = 0;
    private static final int NAV_ONLINE_JUDGE = 1;
    private static final int NAV_ABOUT = 2;
    private static final int NAV_SETTING = 3;

    private static final int NAVIGATION_DRAWER_ITEMS = 4;


    private String[] mDrawerDescriptions;
    private final int mIconImage[] = {
            R.drawable.ic_action_camera,
            R.drawable.ic_action_computer,
            R.drawable.ic_action_about,
            R.drawable.ic_action_settings
    };

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

        mDrawerDescriptions = getResources().getStringArray(R.array.drawer_descriptions);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer_list);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectItem(i);
            }
        });


        mDrawerList.setAdapter(new DrawerAdapter());

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                0,0);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActionBar().setElevation(0);
        }

        selectItem(NAV_CONTEST);

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
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_settings :
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
                return true;
        }

        return false;
    }
    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        //menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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


    private class DrawerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return NAVIGATION_DRAWER_ITEMS;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }


        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = MainActivity.this.getLayoutInflater().inflate(R.layout.drawer_list_item,null);
            }

            ImageView icon = (ImageView) convertView.findViewById(R.id.drawer_icon_view);
            TextView description = (TextView) convertView.findViewById(R.id.drawer_description);

            icon.setImageResource(mIconImage[position]);
            description.setText(mDrawerDescriptions[position]);

            return convertView;
        }
    }

    private void selectItem(int position) {
        Fragment fragment = null;

        switch(position) {
            case NAV_CONTEST :
                fragment = new ContestFragment();
                break;
            case NAV_ONLINE_JUDGE :
                fragment = new OnlineJudgeListFragment();
                break;
            case NAV_SETTING :
                Intent i = new Intent(this,SettingsActivity.class);
                startActivity(i);
        }
        if (position != NAV_SETTING) {
            FragmentManager fragmentManager= getSupportFragmentManager();

            fragmentManager.
                    beginTransaction().
                    replace(R.id.content_frame,fragment).
                    commit();
        }

        mDrawerList.setItemChecked(position,true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    public void onFilterChanged() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ContestFragment) {
            ((ContestFragment)fragment).onFilterChanged();
        }
    }
}
