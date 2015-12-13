package com.forkread.notificationmanager.ui;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.forkread.notificationmanager.R;
import com.forkread.notificationmanager.adapters.NotificationCursorAdapter;
import com.forkread.notificationmanager.database.DatabaseContracts;
import com.forkread.notificationmanager.service.ForkReadNotificationListenerService;


/**
 * Created by dipesh on 03/08/15. Edited by Prateek 01/12/15
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private NotificationCursorAdapter mAdapter;
    private ContentObserver mObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.notifications_list);
//        mRecyclerView = (RecyclerView) findViewById(R.id.notifications_list);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new NotificationCursorAdapter(this, null);
//        mRecyclerView.setAdapter(mAdapter);
//        Intent service = new Intent(this, ForkReadNotificationListenerService.class);
//        startService(service);
//        startLoading();
//        registerContentObserver();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Notification"));
        tabLayout.addTab(tabLayout.newTab().setText("Settings"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_settings) {
//            Intent activity = new Intent(this, SettingsActivity.class);
//            startActivity(activity);
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private LoaderManager.LoaderCallbacks<Cursor> LOADER_CALLBACKS = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(MainActivity.this, DatabaseContracts.Notifications.CONTENT_URI,
                    DatabaseContracts.Notifications.PROJECTION, null, null, DatabaseContracts.Notifications.NOTIFICATION_TIMESTAMP + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) {
                return;
            }
            mAdapter.changeCursor(data);
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
        getContentResolver().registerContentObserver(DatabaseContracts.Notifications.CONTENT_URI, true, mObserver);
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
    public void onDestroy() {
        getContentResolver().unregisterContentObserver(mObserver);
        super.onDestroy();
    }
}
