package com.zpauly.pickers.TimePickers;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.zpauly.pickers.R;
import com.zpauly.pickers.components.TimeHeader;
import com.zpauly.pickers.support.OnButtonClickedListener;

/**
 * Created by root on 16-4-19.
 */
public class TimePickerDialog extends DialogFragment {
    private View mDialog;

    private String IS_DARK_THEME = "IS_DARK_THEME";
    private String SELECT_HOUR = "SELECTED_HOUR";
    private String SELECT_MINUTE = "SELECTED_MINUTE";
    private String IS_AM = "IS_AM";

    private Button mOk;
    private Button mCancel;
    private TimeHeader mTimeHeader;
    private TimePicker mTimePicker;

    private int selectedHour;
    private int selectedMinute;
    private boolean isAM = true;
    private boolean isDarkTheme;

    private int mPrimaryColor;

    private OnButtonClickedListener mOnButtonClickedListener;

    public  static TimePickerDialog newInstance() {
        return newInstance(false);
    }

    public static TimePickerDialog newInstance(boolean isDarkTheme) {
        TimePickerDialog dialog = new TimePickerDialog();
        dialog.isDarkTheme = isDarkTheme;
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(SELECT_HOUR)
                && savedInstanceState.containsKey(SELECT_MINUTE)
                && savedInstanceState.containsKey(IS_DARK_THEME)) {
            selectedHour = savedInstanceState.getInt(SELECT_HOUR);
            selectedMinute = savedInstanceState.getInt(SELECT_MINUTE);
            isAM = savedInstanceState.getBoolean(IS_AM);
            isDarkTheme = savedInstanceState.getBoolean(IS_DARK_THEME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        fetchPrimaryColor();
        /*if (!DevicesUtils.isTablet(getActivity())) {
            mDialog = inflater.inflate(R.layout.time_picker_fragment, container, false);
        } else {
            mDialog = inflater.inflate(R.layout.time_picker_fragment_tablet, container, false);
        }*/
        mDialog = inflater.inflate(R.layout.time_picker_fragment, container, false);

        mOk = (Button) mDialog.findViewById(R.id.ok);
        mCancel = (Button) mDialog.findViewById(R.id.cancel);
        mTimeHeader = (TimeHeader) mDialog.findViewById(R.id.header);
        mTimePicker = (TimePicker) mDialog.findViewById(R.id.picker);

        setupPicker();
        setupHeader();
        setupButtons();

        selectedHour = mTimePicker.getCurrentHour();
        selectedMinute = mTimePicker.getCurrentMinute();

        return mDialog;
    }

    public void setupPicker() {
        if (isDarkTheme) {
            mTimePicker.setDarkTheme(true);
        }
        mTimePicker.setOnTimeSelectedListener(new TimePicker.OnTimeSelectedlistener() {
            @Override
            public void onTimeSelected() {
                if (mTimeHeader.isHourSelected()) {
                    selectedHour = mTimePicker.getCurrentHour();
                    mTimeHeader.setHour(String.valueOf(selectedHour));
                } else {
                    selectedMinute = mTimePicker.getCurrentMinute();
                    mTimeHeader.setMinute(String.valueOf(selectedMinute));
                }
            }
        });
    }

    public void setupHeader() {
        mTimeHeader.setOnHourOrMinuteSelectedListener(new TimeHeader.OnHourOrMinuteSelectedListener() {
            @Override
            public void onHourOrMinuteSeleted() {
                if (mTimeHeader.isHourSelected()) {
                    mTimePicker.setShowHours();
                } else {
                    mTimePicker.setShowMinutes();
                }
            }
        });

        mTimeHeader.setOnAMOrPMSelectedListener(new TimeHeader.OnAMOrPMSelectedListener() {
            @Override
            public void onAMOrPMSelected() {
                if (mTimeHeader.isAMSelected()) {
                    isAM = true;
                } else {
                    isAM = false;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(IS_DARK_THEME, isDarkTheme);
        outState.putInt(SELECT_HOUR, selectedHour);
        outState.putInt(SELECT_MINUTE, selectedMinute);
        outState.putBoolean(IS_AM, isAM);
    }

    private void setupButtons() {
        mCancel.setText("CANCEL");
        mCancel.setTextColor(mPrimaryColor);
        mCancel.setBackground(null);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickedListener != null)
                    mOnButtonClickedListener.onCancelClicked();
                TimePickerDialog.this.dismiss();
            }
        });

        mOk.setText("OK");
        mOk.setTextColor(mPrimaryColor);
        mOk.setBackground(null);

        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnButtonClickedListener != null)
                    mOnButtonClickedListener.onOKClicked();
                TimePickerDialog.this.dismiss();
            }
        });
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = getActivity().obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    public int getSelectedHour() {
        return this.selectedHour;
    }

    public int getSelectedMinute() {
        return this.selectedMinute;
    }

    public boolean isAM() {
        return this.isAM;
    }

    public void setOnButtonClickedListener(OnButtonClickedListener listener) {
        this.mOnButtonClickedListener = listener;
    }
}
