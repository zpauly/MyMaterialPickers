package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpauly.pickers.R;

/**
 * Created by root on 16-4-16.
 */
public class DatePicker extends LinearLayout {
    private Context mContext;

    private boolean isPhone = true;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mPrimaryColor;
    private int mPadding;
    private int mViewWidth;
    private int mViewHeight;

    private FrameLayout mMainLayout;
    private LinearLayout[] mDaysLayout;
    private LinearLayout[] mWeekLayout;

    private TextView mMonth;
    private TextView[] mDays;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DatePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isPhone) {
            mViewWidth = mScreenWidth - 10 * mPadding;
            mViewHeight = mViewWidth;
            setMeasuredDimension(mViewWidth, mViewHeight);
        } else {
            mViewWidth= mScreenHeight - 10 * mPadding;
            mViewHeight = mViewWidth;
            setMeasuredDimension(mViewWidth, mViewHeight);
        }
    }

    private void initView() {
        initParams();

        mMainLayout = new FrameLayout(mContext);
        mDaysLayout = new LinearLayout[3];
        mWeekLayout = new LinearLayout[7];

        mDays = new TextView[49];

        for (int i = 0; i < 7; i++) {
            mWeekLayout[i] = new LinearLayout(mContext);
            mWeekLayout[i].setOrientation(LinearLayout.HORIZONTAL);
        }
        for (int i = 0; i < 49; i++) {
            mDays[i] = new TextView(mContext);
            mDays[i].setWidth(mViewWidth / 7);
            mDays[i].setGravity(Gravity.CENTER);
        }


    }

    private void initParams() {
        fetchPrimaryColor();
        getWindowParams();

        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mContext.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    private void getWindowParams() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        wm.getDefaultDisplay().getSize(point);
        mScreenHeight = point.y;
        mScreenWidth = point.x;
    }
}
