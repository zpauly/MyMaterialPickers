package com.zpauly.pickers.DatePickers;

import android.app.DialogFragment;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zpauly.pickers.R;
import com.zpauly.pickers.components.DateHeader;
import com.zpauly.pickers.support.OnButtonClickedListener;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zpauly on 16-4-23.
 */
public class DatePickerDialog extends DialogFragment {
    private View mDialog;
    private DateHeader mHeader;
    private DatePicker mPicker;
    private Button mCancel;
    private Button mOk;

    private int mPrimaryColor;

    private OnButtonClickedListener mOnButtonClickedListener;

    public static DatePickerDialog newInstance() {
        DatePickerDialog dialog = new DatePickerDialog();
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fetchPrimaryColor();
        mDialog = inflater.inflate(R.layout.date_picker_fragment, container, false);
        mHeader = (DateHeader) mDialog.findViewById(R.id.header);
        mPicker = (DatePicker) mDialog.findViewById(R.id.picker);
        mCancel = (Button) mDialog.findViewById(R.id.cancel);
        mOk = (Button) mDialog.findViewById(R.id.ok);
        setupButtons();
        setupPicker();

        return mDialog;
    }

    private void setupPicker() {
        mHeader.setOnYearOrDaySelectedListener(new DateHeader.OnYearOrDaySelectedListener() {
            @Override
            public void onYearOrDaySelected() {
                if (mHeader.isYearSelected() && !mPicker.isYearShowing()) {
                    mPicker.yearDayChangeAnim();
                } else if (!mHeader.isYearSelected() && mPicker.isYearShowing()) {
                    mPicker.yearDayChangeAnim();
                }
            }
        });
        mPicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onYearSelected() {
                mHeader.setYear(mPicker.getYear());
            }

            @Override
            public void onMonthSelected() {
                SimpleDateFormat day = new SimpleDateFormat("EEE , MMM d");
                mHeader.setDate(day.format(new Date(mPicker.getYear(), mPicker.getMonth() - 1, mPicker.getDay())));
            }

            @Override
            public void onDaySelected() {
                SimpleDateFormat day = new SimpleDateFormat("EEE , MMM d");
                mHeader.setDate(day.format(new Date(mPicker.getYear(), mPicker.getMonth() - 1, mPicker.getDay())));
            }
        });
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
                DatePickerDialog.this.dismiss();
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
                DatePickerDialog.this.dismiss();
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

    public void setOnbuttonClickedListener(OnButtonClickedListener listener) {
        mOnButtonClickedListener = listener;
    }

    public int getYear() {
        return mPicker.getYear();
    }

    public int getMonth() {
        return mPicker.getMonth();
    }

    public int getDay() {
        return mPicker.getDay();
    }
}
