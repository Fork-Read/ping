package com.forkread.notificationmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dipesh on 02/08/15.
 */
public class NotificationDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notification.db";
    private static NotificationDatabaseHelper sNotificationDatabaseHelper;

    public static NotificationDatabaseHelper getInstance(Context context) {
        if (sNotificationDatabaseHelper == null) {
            sNotificationDatabaseHelper = new NotificationDatabaseHelper(context);
        }
        return sNotificationDatabaseHelper;
    }

    public NotificationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createNotificationsTable(db);
        createAppSettingsTable(db);
        createProfileTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createNotificationsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DatabaseContracts.Notifications.TABLE_NAME
                + " (" + DatabaseContracts.Notifications._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContracts.Notifications.NOTIFICATION_APP_NAME + " TEXT NOT NULL, "
                + DatabaseContracts.Notifications.NOTIFICATION_TEXT + " TEXT, "
                + DatabaseContracts.Notifications.NOTIFICATION_TICKER_TEXT + " TEXT, "
                + DatabaseContracts.Notifications.NOTIFICATION_TIMESTAMP + " INTEGER NOT NULL, "
                + DatabaseContracts.Notifications.NOTIFICATION_ID + " INTEGER, "
                + DatabaseContracts.Notifications.NOTIFICATION_TITLE + " TEXT);");
    }

    private void createAppSettingsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DatabaseContracts.AppSettings.TABLE_NAME
                + " (" + DatabaseContracts.AppSettings._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContracts.AppSettings.PACKAGE_NAME + " TEXT NOT NULL, "
                + DatabaseContracts.AppSettings.FLAGS + " INTEGER DEFAULT 0, "
                + DatabaseContracts.AppSettings.PROFILE + " INTEGER DEFAULT 0);");
    }

    private void createProfileTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + DatabaseContracts.Profile.TABLE_NAME
                + " (" + DatabaseContracts.Profile._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContracts.Profile.NAME + " TEXT NOT NULL, "
                + DatabaseContracts.Profile.START_TIME + " INTEGER DEFAULT 0, "
                + DatabaseContracts.Profile.END_TIME + " INTEGER DEFAULT 0);");
    }
}
