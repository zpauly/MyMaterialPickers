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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zpauly.pickers.R;
import com.zpauly.pickers.utils.ColorUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by root on 16-4-19.
 */
public class Header extends LinearLayout {
    private boolean isPhone = true;
    private boolean selectHours = true;
    private boolean selectMinutes = false;
    private boolean selectAm;
    private boolean selectPm;
    private int mTimeSize;
    private int mAmPmSize;
    private int mHorizontalPadding;

    private Context mContext;

    private int mPrimaryColor;
    private int mScreenWidth;
    private int mScreenHeight;
    private int currentHour;
    private int currentMinute;

    private LinearLayout mTimeLayout;
    private LinearLayout mAMOrPMLayout;
    private TextView mHour;
    private TextView mMinute;
    private TextView mColon;
    private TextView mAM;
    private TextView mPM;

    public Header(Context context) {
        this(context, null);
    }

    public Header(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Header(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    public Header(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        if (isPhone) {
            width = mScreenWidth - 12 * mHorizontalPadding;
            height = mTimeLayout.getHeight() + getPaddingTop() + getPaddingBottom();
        } else {
            height = mScreenHeight - 12 * mHorizontalPadding;
            width = mTimeLayout.getWidth() + getPaddingLeft() + getPaddingRight();
        }
        setMeasuredDimension(width, height);
    }

    private void initView() {
        initParams();
        mTimeLayout = new LinearLayout(mContext);
        mAMOrPMLayout = new LinearLayout(mContext);
        mHour = new TextView(mContext);
        mMinute = new TextView(mContext);
        mColon = new TextView(mContext);
        mAM = new TextView(mContext);
        mPM = new TextView(mContext);

        if (isPhone) {
            setOrientation(LinearLayout.HORIZONTAL);
        } else {
            setOrientation(LinearLayout.VERTICAL);
        }
        setGravity(Gravity.CENTER);

        LayoutParams timeLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initTimeLayout();
        addView(mTimeLayout, -1, timeLayoutParams);

        LayoutParams amOrPmLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initAmOrPmLayout();
        addView(mAMOrPMLayout, -1, amOrPmLayoutParams);

    }

    @SuppressWarnings("deprecation")
    private void initParams() {
        mHorizontalPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        mTimeSize = getResources().getDimensionPixelSize(R.dimen.time_text_size);
        mAmPmSize = getResources().getDimensionPixelSize(R.dimen.am_pm_text_size);

        getWindowParams();
        fetchPrimaryColor();

        Date current = new Date();
        currentHour = current.getHours();
        currentMinute = current.getMinutes();

        GregorianCalendar ca = new GregorianCalendar();
        if (ca.get(GregorianCalendar.AM_PM) == 0) {
            selectAm = true;
            selectPm = false;
        } else {
            selectAm = false;
            selectPm = true;
        }
    }

    private void initTimeLayout() {
        mTimeLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams hourLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initHour();
        mTimeLayout.addView(mHour, -1, hourLayoutParams);

        LinearLayout.LayoutParams colonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initColon();
        mTimeLayout.addView(mColon, -1, colonLayoutParams);

        LinearLayout.LayoutParams minuteLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initMinute();
        mTimeLayout.addView(mMinute, -1, minuteLayoutParams);
    }

    private void initHour() {
        mHour.setText(currentHour);
        if (selectHours) {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
        mColon.setTextSize(mTimeSize);
    }

    private void initColon() {
        mColon.setText(" : ");
        mColon.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        mColon.setTextSize(mTimeSize);
    }

    private void initMinute() {
        mMinute.setText(currentMinute);
        if (selectMinutes) {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
        mColon.setTextSize(mTimeSize);
    }

    private void initAmOrPmLayout() {
        mAMOrPMLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams amLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initAm();
        mAMOrPMLayout.addView(mAM, -1, amLayoutParams);

        LayoutParams pmLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initPm();
        mAMOrPMLayout.addView(mPM, -1, pmLayoutParams);
    }

    private void initAm() {
        mAM.setText(getResources().getString(R.string.am));
        mAM.setTextSize(mAmPmSize);
        if (selectAm) {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
    }

    private void initPm() {
        if (selectPm) {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
        mPM.setText(getResources().getString(R.string.pm));
        mPM.setTextSize(mAmPmSize);
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
}
