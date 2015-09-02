package com.forkread.notificationmanager.ui;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.forkread.notificationmanager.R;
import com.forkread.notificationmanager.Utils;

import java.net.URLEncoder;
import java.util.Calendar;

/**
 * Created by dipesh on 07/08/15.
 */
public class SettingsActivity extends AppCompatActivity {
    private Button mStartTime;
    private Button mEndTime;
    private CheckBox mEnableNightMode;
    private CheckBox mEnableCustomMode;
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private int mSelectedStartHour;
    private int mSelectedStartMinute;
    private int mSelectedEndHour;
    private int mSelectedEndMinute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        mPreferences = getSharedPreferences(Utils.PREF_KEY, Context.MODE_MULTI_PROCESS);
        mEditor = mPreferences.edit();
        mStartTime = (Button) findViewById(R.id.start_time);
        mEndTime = (Button) findViewById(R.id.end_time);
        mEnableNightMode = (CheckBox) findViewById(R.id.enable_night_mode);
        mEnableCustomMode = (CheckBox) findViewById(R.id.enable_custom_mode);
        mStartTime.setEnabled(false);
        mEndTime.setEnabled(false);
        init();
    }

    private void init() {
        mEnableNightMode.setChecked(mPreferences.getBoolean(Utils.PREF_KEY_NIGHT_MODE_ENABLED, false));
        mEnableNightMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEditor.putBoolean(Utils.PREF_KEY_NIGHT_MODE_ENABLED, isChecked).commit();
            }
        });

        if (mPreferences.getBoolean(Utils.PREF_KEY_CUSTOM_SETTING_ENABLED, false)) {
            mEnableCustomMode.setChecked(true);
            mStartTime.setEnabled(true);
            mEndTime.setEnabled(true);
            setDates();
        } else {
            mEnableCustomMode.setChecked(false);
            mStartTime.setText(DateUtils.getRelativeTimeSpanString(this, System.currentTimeMillis()));
            mEndTime.setText(DateUtils.getRelativeTimeSpanString(this, System.currentTimeMillis()));
        }
        mEnableCustomMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mStartTime.setEnabled(true);
                    mEndTime.setEnabled(true);
                } else {
                    mStartTime.setEnabled(false);
                    mEndTime.setEnabled(false);
                }
            }
        });
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = "";
                        if (selectedHour / 10 == 0) {
                            hour = "0" + selectedHour;
                        } else {
                            hour += selectedHour;
                        }
                        String minute = "";
                        if (selectedHour / 10 == 0) {
                            minute = "0" + selectedMinute;
                        } else {
                            minute += selectedMinute;
                        }
                        mSelectedStartHour = selectedHour;
                        mSelectedStartMinute = selectedMinute;
                        mStartTime.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Start Time");
                mTimePicker.show();
            }
        });
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(SettingsActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String hour = "";
                        if (selectedHour / 10 == 0) {
                            hour = "0" + selectedHour;
                        } else {
                            hour += selectedHour;
                        }
                        String minute = "";
                        if (selectedHour / 10 == 0) {
                            minute = "0" + selectedMinute;
                        } else {
                            minute += selectedMinute;
                        }
                        mSelectedEndHour = selectedHour;
                        mSelectedEndMinute = selectedMinute;
                        mEndTime.setText(hour + ":" + minute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select End Time");
                mTimePicker.show();
            }
        });
    }

    private void setDates() {
        mSelectedStartHour = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_HOUR, 0);
        mSelectedStartMinute = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_MINUTE, 0);
        setTextDate(mStartTime, mSelectedStartHour, mSelectedStartMinute);
        mSelectedEndHour = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_HOUR, 0);
        mSelectedEndMinute = mPreferences.getInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_MINUTE, 0);
        setTextDate(mEndTime, mSelectedEndHour, mSelectedEndMinute);
    }

    private void setTextDate(TextView view, int selectedHour, int selectedMinute) {
        String hour = "";
        if (selectedHour / 10 == 0) {
            hour = "0" + selectedHour;
        } else {
            hour += selectedHour;
        }
        String minute = "";
        if (selectedHour / 10 == 0) {
            minute = "0" + selectedMinute;
        } else {
            minute += selectedMinute;
        }
        view.setText(hour + ":" + minute);
    }

    @Override
    public void onDestroy() {
        if (mEnableCustomMode.isChecked()) {
            mEditor.putBoolean(Utils.PREF_KEY_CUSTOM_SETTING_ENABLED, true);
            mEditor.putInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_HOUR, mSelectedStartHour);
            mEditor.putInt(Utils.PREF_KEY_CUSTOM_SETTING_START_TIME_MINUTE, mSelectedStartMinute);
            mEditor.putInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_HOUR, mSelectedEndHour);
            mEditor.putInt(Utils.PREF_KEY_CUSTOM_SETTING_END_TIME_MINUTE, mSelectedEndMinute);
        } else {
            mEditor.putBoolean(Utils.PREF_KEY_CUSTOM_SETTING_ENABLED, false);
        }
        mEditor.commit();
        super.onDestroy();
    }
}
