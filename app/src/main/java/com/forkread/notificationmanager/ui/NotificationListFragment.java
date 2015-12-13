package com.forkread.notificationmanager.ui;
import com.forkread.notificationmanager.R;
import com.forkread.notificationmanager.adapters.NotificationCursorAdapter;
import com.forkread.notificationmanager.database.DatabaseContracts;
import com.forkread.notificationmanager.service.ForkReadNotificationListenerService;

/**
 * Created by prateek on 13/12/15.
 */

import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class NotificationListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private NotificationCursorAdapter mAdapter;
    private ContentObserver mObserver;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_list_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.notifications_list);

        return rootView;
    }

    private LoaderManager.LoaderCallbacks<Cursor> LOADER_CALLBACKS = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(getActivity(), DatabaseContracts.Notifications.CONTENT_URI,
                    DatabaseContracts.Notifications.PROJECTION, null, null, DatabaseContracts.Notifications.NOTIFICATION_TIMESTAMP + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) {
                return;
            }
            mAdapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    private void startLoading() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.restartLoader(0, null, LOADER_CALLBACKS);
    }

    private void registerContentObserver() {
        if (mObserver == null) {
            mObserver = new NotificationsDataChangeObserver(new Handler());
        }
        getActivity().getContentResolver().registerContentObserver(DatabaseContracts.Notifications.CONTENT_URI, true, mObserver);
    }

    private class NotificationsDataChangeObserver extends ContentObserver {

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public NotificationsDataChangeObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            startLoading();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NotificationCursorAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);
        Intent service = new Intent(getActivity(), ForkReadNotificationListenerService.class);
        getActivity().startService(service);
        startLoading();
        registerContentObserver();
    }
    @Override
    public void onDestroy() {
        getActivity().getContentResolver().unregisterContentObserver(mObserver);
        super.onDestroy();
    }

}
