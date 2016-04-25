package com.zpauly.pickers.DatePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * Created by root on 16-4-16.
 */
public class DatePicker extends LinearLayout {
    private Context mContext;

    private int mPrimaryColor;

    private LinearLayout mContainer;

    private DayPicker mDayPicker;
    private YearPicker mYearPicker;

    private boolean isYearShowing = true;

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
        setMeasuredDimension(mYearPicker.getMeasuredWidth(), mYearPicker.getMeasuredHeight());
    }

    private void initView() {
        initParams();
        setOrientation(VERTICAL);

        mContainer = new LinearLayout(mContext);
        mDayPicker = new DayPicker(mContext);
        mYearPicker = new YearPicker(mContext);

        mContainer.setOrientation(VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mContainer, -1, layoutParams);

        setupYearPicker();
        setupDayPicker();
    }

    private void initParams() {
        fetchPrimaryColor();
    }

    private void setupYearPicker() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.addView(mYearPicker, -1, layoutParams);
    }

    private void setupDayPicker() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mContainer.addView(mDayPicker, -1, layoutParams);
    }

    private void fetchPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = mContext.obtainStyledAttributes(typedValue.data, new int[]{android.R.attr.colorPrimary});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        mPrimaryColor = color;
    }

    public void YearDayChangeAnim() {
        if (isYearShowing) {
            isYearShowing = false;
            ViewPropertyAnimator.animate(mContainer).translationY(-mYearPicker.getMeasuredHeight()).start();
        } else {
            isYearShowing = true;
            ViewPropertyAnimator.animate(mContainer).translationY(0).start();
        }
    }

    public boolean isYearShowing() {
        return this.isYearShowing;
    }

    private Drawable drawBg() {
        ShapeDrawable shapeDrawable = new ShapeDrawable(new OvalShape());
        shapeDrawable.getPaint().setColor(mPrimaryColor);
        return shapeDrawable;
    }
}
