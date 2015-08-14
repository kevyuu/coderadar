package com.kayulab.android.coderadar.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kayulab.android.coderadar.R;
import com.kayulab.android.coderadar.data.ContestContract;
import com.kayulab.android.coderadar.sync.SyncErrorStatusReceiver;

public abstract class ContestListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, SyncStatusObserver {

    //private OnFragmentInteractionListener mListener;
    private ListView mContestListView;
    private ContestAdapter mContestAdapter;
    private SyncErrorStatusReceiver mSyncErrorStatusReceiver;
    private ViewGroup mSyncProgressContainer;
    private TextView mNoContestTextView;

    private Object mContentProviderHandle;

    private static final int CONTEST_LOADER_ID = 0;

    protected static final String[] CONTEST_SUMMARY_COLUMNS = {
            ContestContract.ContestEntry._ID,
            ContestContract.ContestEntry.COLUMN_TITLE,
            ContestContract.ContestEntry.COLUMN_SOURCE,
            ContestContract.ContestEntry.COLUMN_START_TIME
    };


    static final int COL_CONTEST_ID = 0;
    static final int COL_CONTEST_TITLE = 1;
    static final int COL_CONTEST_SOURCE = 2;
    static final int COL_CONTEST_START_TIME = 3;

    public interface Callback {
        public void onItemSelected(Uri contestUri,ImageView logo);
    }

    // TODO: Rename and change types and number of parameters

    public ContestListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_contest_list, container, false);

        mContestAdapter = new ContestAdapter(getActivity(),null,0);

        mContestListView = (ListView) view.findViewById(R.id.contest_listView);
        mContestListView.setAdapter(mContestAdapter);

        mSyncProgressContainer = (ViewGroup) view.findViewById(R.id.sync_progress_container);
        mNoContestTextView = (TextView) view.findViewById(R.id.no_contest_textView);

        mContestListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                ImageView logo = (ImageView) view.findViewById(R.id.onlineJudge_image);

                if (cursor!=null) {
                    Long contestId = cursor.getLong(COL_CONTEST_ID);
                    ((Callback) getActivity())
                            .onItemSelected(ContestContract.ContestEntry.buildContestUriWithId(contestId),logo);
                }

            }
        });

        mSyncErrorStatusReceiver = new SyncErrorStatusReceiver() {

            @Override
            public void onSyncError() {
                Toast.makeText(getActivity(),getActivity().getString(R.string.sync_error),Toast.LENGTH_SHORT).show();
            }
        };
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(CONTEST_LOADER_ID, null, this);
        getActivity().registerReceiver(mSyncErrorStatusReceiver, SyncErrorStatusReceiver.getIntentFilter());
        mContentProviderHandle = ContentResolver.addStatusChangeListener(
                ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE,this);
        refreshSyncProgressView();
    }

    @Override
    public void onPause() {
        super.onPause();
        ContentResolver.removeStatusChangeListener(mContentProviderHandle);
        getActivity().unregisterReceiver(mSyncErrorStatusReceiver);
    }

    public void onFilterChanged() {
        getLoaderManager().restartLoader(CONTEST_LOADER_ID, null, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CONTEST_LOADER_ID,null,this);
    }


    @Override
    public abstract Loader<Cursor> onCreateLoader(int id, Bundle args);

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mContestAdapter.swapCursor(data);
        if (data.getCount()==0) {
            mNoContestTextView.setVisibility(View.VISIBLE);
        } else {
            mNoContestTextView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mContestAdapter.swapCursor(null);
    }


    @Override
    public void onStatusChanged(int which) {

        updateRefresh(isSyncing());
    }

    private boolean isSyncing() {
        String accountType = getActivity().getString(R.string.sync_account_type);

        AccountManager accountManager = AccountManager.get(getActivity());
        Account[] accounts = accountManager
                .getAccountsByType(accountType);

        if (accounts.length <= 0) {
            return false;
        }

        return ContentResolver.isSyncActive(accounts[0],
                getActivity().getString(R.string.content_authority));
    }

    public void updateRefresh(final boolean isSyncing) {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (isSyncing) {
                    mSyncProgressContainer.setVisibility(View.VISIBLE);
                } else {
                    mSyncProgressContainer.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void refreshSyncProgressView() {
        if (isSyncing()) {
            mSyncProgressContainer.setVisibility(View.VISIBLE);
        } else {
            mSyncProgressContainer.setVisibility(View.INVISIBLE);
        }
    }

}
