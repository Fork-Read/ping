package com.forkread.notificationmanager.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by dipesh on 03/08/15.
 */
public class NotificationsContentProvider extends ContentProvider {
    private static final int NOTIFICATIONS = 1;
    private static final UriMatcher sUriMatcher = new UriMatcher(-1);
    private Context mContext;
    private SQLiteDatabase mDb;
    private NotificationDatabaseHelper mDbHelper;

    static {
        sUriMatcher.addURI(DatabaseContracts.AUTHORITY, DatabaseContracts.Notifications.TABLE_NAME, NOTIFICATIONS);
    }

    @Override
    public boolean onCreate() {
        mContext = getContext();
        mDbHelper = NotificationDatabaseHelper.getInstance(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                tableName = DatabaseContracts.Notifications.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("no such table found");
        }
        return mDb.query(tableName, projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                tableName = DatabaseContracts.Notifications.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("no such table found");
        }
        Uri insertUri = ContentUris.withAppendedId(DatabaseContracts.Notifications.CONTENT_URI, mDb.insert(tableName, null, values));
        mContext.getContentResolver().notifyChange(uri, null);
        return insertUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case NOTIFICATIONS:
                tableName = DatabaseContracts.Notifications.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("no such table found");
        }
        return mDb.delete(tableName, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
