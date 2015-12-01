package com.forkread.notificationmanager.adapters;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.forkread.notificationmanager.R;

/**
 * Created by dipesh on 03/08/15.
 */
public class NotificationCursorAdapter extends CursorRecyclerAdapter {
    private Context mContext;
    private PackageManager mPkgManager;
    private LayoutInflater mInflater;

    public NotificationCursorAdapter(Context context, Cursor c) {
        super(c);
        mContext = context;
        mPkgManager = mContext.getPackageManager();
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.notification_list_item, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolderCursor(RecyclerView.ViewHolder holder, Cursor cursor) {
        CustomViewHolder viewHolder = (CustomViewHolder) holder;
        String packageName = cursor.getString(4);
        try {
            viewHolder.mAppName.setText(mPkgManager.getApplicationLabel(mPkgManager.getApplicationInfo(packageName, 0)));
            viewHolder.mAppIcon.setImageDrawable(mPkgManager.getApplicationIcon(packageName));
        } catch (PackageManager.NameNotFoundException e) {
            viewHolder.mAppIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_launcher));
            viewHolder.mAppName.setText("");
        }
        viewHolder.mText.setText(cursor.getString(2) + cursor.getString(3));
        //viewHolder.mTitle.setText(cursor.getString(2));
//        viewHolder.mTickerText.setText(cursor.getString(1));
        viewHolder.mNotificationtime.setText(DateUtils.getRelativeTimeSpanString(mContext, cursor.getLong(5)).toString());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView mAppIcon;
        protected TextView mTitle;
        protected TextView mTickerText;
        protected TextView mText;
        protected TextView mNotificationtime;
        protected TextView mAppName;

        public CustomViewHolder(View view) {
            super(view);
            mAppIcon = (ImageView) view.findViewById(R.id.app_icon);
            //mTitle = (TextView) view.findViewById(R.id.notification_title);
//            mTickerText = (TextView) view.findViewById(R.id.notification_ticker);
            mText = (TextView) view.findViewById(R.id.notification_text);
            mNotificationtime = (TextView) view.findViewById(R.id.notification_time);
            mAppName = (TextView) view.findViewById(R.id.app_name_view);
        }
    }
}
