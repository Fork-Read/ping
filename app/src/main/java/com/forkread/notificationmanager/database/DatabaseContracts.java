package com.forkread.notificationmanager.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by dipesh on 03/08/15.
 */
public class DatabaseContracts {
    public static final String AUTHORITY = "com.forkread.notifications";

    public interface NotificationColumns extends BaseColumns {
        /**
         *  verion 1 columns
         */
        /**
         * package name of the app like com.whatsapp
         */
        String NOTIFICATION_APP_NAME = "notificationPkgName";
        /**
         * notification title
         */
        String NOTIFICATION_TITLE = "notification_title";
        /**
         * ticker text
         */
        String NOTIFICATION_TICKER_TEXT = "notification_ticker_text";
        /**
         * timestamp of the notification
         */
        String NOTIFICATION_TIMESTAMP = "notification_timestamp";
        /**
         * notification text
         */
        String NOTIFICATION_TEXT = "notification_text";
        /**
         * notification id
         */
        String NOTIFICATION_ID = "notification_id";
    }

    public static final class Notifications implements NotificationColumns {
        public static final String TABLE_NAME = "notifications";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/notifications");
        public static final String[] PROJECTION = {_ID, NOTIFICATION_TITLE, NOTIFICATION_TICKER_TEXT, NOTIFICATION_TEXT,
                NOTIFICATION_APP_NAME, NOTIFICATION_TIMESTAMP};
    }

    public interface AppsettingsColumns extends BaseColumns {
        String PACKAGE_NAME = "package_name";
        String FLAGS = "flags";
        String PROFILE = "PROFILE";

        int FLAG_SILENT_NOTIFICATION = 1;
        int FLAG_NEVER_SHOW_NOTIFICATION = 2;
    }

    public static final class AppSettings implements AppsettingsColumns {
        public static final String TABLE_NAME = "app_settings";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/app_settings");
    }

    public interface ProfileColumns extends BaseColumns {
        String NAME = "name";
        String START_TIME = "start_time";
        String END_TIME = "end_time";
    }

    public static final class Profile implements ProfileColumns {
        public static final String TABLE_NAME = "profile";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/profile");
    }
}
