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
import com.zpauly.pickers.utils.DevicesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    private int mPadding;
    private int mComponentsMargin;
    private int mLayoutMargin;

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

    private OnHourOrMinuteSelectedListener mOnHourOrMinuteSelectedListener;
    private OnAMOrPMSelectedListener mOnAMOrPMSelectedListener;

    public interface OnHourOrMinuteSelectedListener {
        void onHourOrMinuteSeleted();
    }

    public interface OnAMOrPMSelectedListener {
        void onAMOrPMSelected();
    }

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
        getWindowParams();
        if (isPhone) {
            int width = mScreenWidth - 10 * mPadding;
            int height = mTimeLayout.getHeight() + getPaddingTop() + getPaddingBottom();
            setMeasuredDimension(width, height);
        } else {
            int height = mScreenHeight - 10 * mPadding;
            int width = mTimeLayout.getWidth() + getPaddingLeft() + getPaddingRight();
            setMeasuredDimension(width, height);
        }
    }

    private void initView() {
        /*isPhone = DevicesUtils.isTablet(mContext);*/
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
        timeLayoutParams.setMargins(0, mLayoutMargin, mComponentsMargin, mLayoutMargin);
        initTimeLayout();
        addView(mTimeLayout, -1, timeLayoutParams);

        LayoutParams amOrPmLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        timeLayoutParams.setMargins(mComponentsMargin * 5, mLayoutMargin, 0, mLayoutMargin);
        initAmOrPmLayout();
        addView(mAMOrPMLayout, -1, amOrPmLayoutParams);

        setBackgroundColor(mPrimaryColor);

        setSelected();
    }

    @SuppressWarnings("deprecation")
    private void initParams() {
        mPadding = getResources().getDimensionPixelOffset(R.dimen.horizontal_padding);
        mTimeSize = getResources().getDimensionPixelSize(R.dimen.time_text_size);
        mAmPmSize = getResources().getDimensionPixelSize(R.dimen.am_pm_text_size);
        mComponentsMargin = getResources().getDimensionPixelOffset(R.dimen.header_components_margin);
        mLayoutMargin = getResources().getDimensionPixelOffset(R.dimen.header_layout_margin);

        getWindowParams();
        fetchPrimaryColor();

        Date current = new Date();
        currentHour = current.getHours();
        currentMinute = current.getMinutes();

        SimpleDateFormat ft = new SimpleDateFormat("aa");
        if (ft.format(current).equals("AM")) {
            selectAm = true;
            selectPm = false;
        } else {
            selectAm = false;
            selectPm = true;
        }
    }

    private void initTimeLayout() {
        mTimeLayout.setOrientation(LinearLayout.HORIZONTAL);
        mTimeLayout.setBackground(null);

        LayoutParams hourLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initHour();
        mTimeLayout.addView(mHour, -1, hourLayoutParams);

        LayoutParams colonLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initColon();
        mTimeLayout.addView(mColon, -1, colonLayoutParams);

        LayoutParams minuteLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initMinute();
        mTimeLayout.addView(mMinute, -1, minuteLayoutParams);
    }

    private void initHour() {
        mHour.setText(String.valueOf(currentHour));
        if (selectHours) {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
        mHour.setTextSize(mTimeSize);
    }

    private void initColon() {
        mColon.setText(" : ");
        mColon.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        mColon.setTextSize(mTimeSize);
    }

    private void initMinute() {
        mMinute.setText(String.valueOf(currentMinute));
        if (selectMinutes) {
            mMinute.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mMinute.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
        mMinute.setTextSize(mTimeSize);
    }

    private void initAmOrPmLayout() {
        mAMOrPMLayout.setOrientation(LinearLayout.VERTICAL);
        mAMOrPMLayout.setBackground(null);

        LayoutParams amLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        initAm();
        mAMOrPMLayout.addView(mAM, -1, amLayoutParams);

        LayoutParams pmLayoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pmLayoutParams.setMargins(0, mComponentsMargin, 0, mComponentsMargin);
        initPm();
        mAMOrPMLayout.addView(mPM, -1, pmLayoutParams);
    }

    private void initAm() {
        mAM.setText(getResources().getString(R.string.am));
        mAM.setTextSize(mAmPmSize);
        if (selectAm) {
            mAM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mAM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
        }
    }

    private void initPm() {
        if (selectPm) {
            mPM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
        } else {
            mPM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
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

    public void setHour(CharSequence sequence) {
        mHour.setText(sequence);
    }

    public void setMinute(CharSequence sequence) {
        mMinute.setText(sequence);
    }

    public void setOnHourOrMinuteSelectedListener(OnHourOrMinuteSelectedListener listener) {
        mOnHourOrMinuteSelectedListener = listener;
    }

    public void setOnAMOrPMSelectedListener(OnAMOrPMSelectedListener listener) {
        mOnAMOrPMSelectedListener = listener;
    }

    public boolean isHourSelected() {
        return this.selectHours;
    }

    public boolean isAMSelected() {
        return this.selectAm;
    }

    public void setSelected() {
        mHour.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectHours = true;
                selectMinutes = false;
                mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                mMinute.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                if (mOnHourOrMinuteSelectedListener != null) {
                    mOnHourOrMinuteSelectedListener.onHourOrMinuteSeleted();
                }
            }
        });

        mMinute.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMinutes = true;
                selectHours = false;
                mHour.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                mMinute.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                if (mOnHourOrMinuteSelectedListener != null) {
                    mOnHourOrMinuteSelectedListener.onHourOrMinuteSeleted();
                }
            }
        });

        mAM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAm = true;
                selectPm = false;
                mAM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                mPM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                if (mOnAMOrPMSelectedListener != null) {
                    mOnAMOrPMSelectedListener.onAMOrPMSelected();
                }
            }
        });

        mPM.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPm = true;
                selectAm = false;
                mAM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.54f));
                mPM.setTextColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.87f));
                if (mOnAMOrPMSelectedListener != null) {
                    mOnAMOrPMSelectedListener.onAMOrPMSelected();
                }
            }
        });
    }
}
