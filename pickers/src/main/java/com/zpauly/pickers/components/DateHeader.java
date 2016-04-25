package com.zpauly.pickers.components;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpauly.pickers.R;
import com.zpauly.pickers.utils.ColorUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 16-4-23.
 */
public class DateHeader extends LinearLayout {
    private Context mContext;

    private int mScreenHeight;
    private int mScreenWidth;
    private int mPrimaryColor;
    private int mPadding;
    private int mYearSize;
    private int mDateSize;
    private int mViewHeight;
    private int mViewWidth;

    private boolean isYearSelected = true;

    private TextView mYear;
    private TextView mDate;

    private OnYearOrDaySelectedListener mOnYearOrDaySelectedListener;

    public interface OnYearOrDaySelectedListener {
        void onYearOrDaySelected();
    }

    public DateHeader(Context context) {
        this(context, null);
    }

    public DateHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateHeader(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = mScreenWidth - 10 * mPadding;
        mViewHeight = mDate.getHeight() + mYear.getHeight() + getPaddingBottom() + getPaddingTop();
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    private void initView() {
        initParams();
        setBackgroundColor(mPrimaryColor);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        setPadding( 3 * mPadding, mPadding, 3 * mPadding, mPadding);

        Date date = new Date();

        mYear = new TextView(mContext);
        mDate = new TextView(mContext);
        SimpleDateFormat year = new SimpleDateFormat("yyyy");
        SimpleDateFormat day = new SimpleDateFormat("EEE , MMM d");

        mYear.setText(year.format(date));
        mDate.setText(day.format(date));
        mYear.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        mDate.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        mYear.setTextSize(mYearSize);
        mDate.setTextSize(mDateSize);

        mYear.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                mDate.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                isYearSelected = true;
                if (mOnYearOrDaySelectedListener != null) {
                    mOnYearOrDaySelectedListener.onYearOrDaySelected();
                }
            }
        });

        mDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                mDate.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                isYearSelected = false;
                if (mOnYearOrDaySelectedListener != null) {
                    mOnYearOrDaySelectedListener.onYearOrDaySelected();
                }
            }
        });

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mYear, -1, layoutParams);
        addView(mDate, -1, layoutParams);
    }

    private void initParams() {
        getWindowParams();
        fetchPrimaryColor();

        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        mYearSize = getResources().getDimensionPixelSize(R.dimen.simple_year_text_size);
        mDateSize = getResources().getDimensionPixelOffset(R.dimen.header_date_text_size);
    }

    private void getWindowParams() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
        mScreenWidth = point.x;
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mContext.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    public void setOnYearOrDaySelectedListener(OnYearOrDaySelectedListener listener) {
        mOnYearOrDaySelectedListener = listener;
    }

    public boolean isYearSelected() {
        return this.isYearSelected;
    }

    public void setYear(int year) {
        this.mYear.setText(String.valueOf(year));
    }

    public int getYear() {
        return Integer.parseInt(mYear.getText().toString());
    }

    public void setDate() {

    }
}
