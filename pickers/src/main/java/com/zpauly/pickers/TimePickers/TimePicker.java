package com.zpauly.pickers.TimePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.zpauly.pickers.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 16-4-16.
 */
public class TimePicker extends FrameLayout {
    private Context mContext;

    private boolean isDarkTheme = false;

    private int mPrimaryColor;
    private final int mHorizontalPadding = 24;
    private float clockRadius;
    private float textRadius;
    private float centerX;
    private float centerY;
    private boolean isHours = true;
    private boolean isMinutes = false;
    private Point presentTime;
    private int currentHour;
    private int currentMinute;
    private int currentAngle;
    private Point touchTime = new Point(0, 0);
    private List<Point> clockTime = new ArrayList<>();

    private int mScreeWidth;
    private int mScreeHeight;

    private ClockHand mClockHand;
    private Clock mClock;
    private ClockText mClockText;

    private Paint mPaint = new Paint();

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TimePicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        getWindowParams();
        initArguments();
        mClock = new Clock(mContext);
        mClockHand = new ClockHand(mContext);
        mClockText = new ClockText(mContext);

        FrameLayout.LayoutParams clockLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        clockLayoutParams.gravity = Gravity.CENTER;
        clockLayoutParams.setMargins(mHorizontalPadding, mHorizontalPadding, mHorizontalPadding, mHorizontalPadding);
        addView(mClock, -1, clockLayoutParams);

        FrameLayout.LayoutParams handLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        handLayoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.END;
        handLayoutParams.setMargins(2 * mHorizontalPadding, 2 * mHorizontalPadding, 2 * mHorizontalPadding, 2 * mHorizontalPadding);
        addView(mClockHand, -1, handLayoutParams);

        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        textLayoutParams.setMargins(mHorizontalPadding, mHorizontalPadding, mHorizontalPadding, mHorizontalPadding);
        addView(mClockText, -1, textLayoutParams);
    }

    private void initArguments() {
        fetchPrimaryColor();

        Date date = new Date();
        currentHour = date.getHours();
        currentMinute = date.getMinutes();

        Log.i("hour", String.valueOf(currentHour));
        Log.i("minute", String.valueOf(currentMinute));

        clockRadius = (mScreeWidth - 4 * mHorizontalPadding) / 2;
        centerX = clockRadius;
        centerY = centerX;

        textRadius = clockRadius - 2 * mHorizontalPadding;
        final double oneAngle = 30;
        double totalAngle = 0;
        if (isHours) {
            presentTime = new Point((int) (centerX + textRadius * Math.sin(currentHour * 30 * (Math.PI / 180))),
                    (int) (centerY - textRadius * Math.cos(currentHour * 30 * (Math.PI / 180))));
            currentAngle = currentHour * 30;
        }
        if (isMinutes) {
            presentTime = new Point((int) (centerX + textRadius * Math.sin(currentMinute * 6 * (Math.PI / 180))),
                    (int) (centerY - textRadius * Math.cos(currentMinute * 6 * (Math.PI / 180))));
            currentAngle = currentMinute * 6;
        }
        for (int i = 0; i < 12; i++) {
            totalAngle += oneAngle;
            float textX = (float) (textRadius * Math.sin(totalAngle * (Math.PI / 180)));
            float textY = (float) (textRadius * Math.cos(totalAngle * (Math.PI / 180)));
            float x = centerX + textX;
            float y = centerY - textY;
            Point point = new Point((int) x - 15, (int) y + 15);
            clockTime.add(point);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        getWindowParams();
        int width = mScreeWidth - 2 * mHorizontalPadding;
        int height = width;
        setMeasuredDimension(width, height);
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
        mScreeHeight = point.y;
        mScreeWidth = point.x;
    }

    public void setDarkTheme(boolean darkTheme) {
        this.isDarkTheme = darkTheme;
        invalidate();
    }

    public boolean isDarkTheme() {
        return this.isDarkTheme;
    }

    public void setShowHours(boolean isHours) {
        this.isHours = true;
        this.isMinutes = false;
        invalidate();
    }

    public void setShowMinutes(boolean isMinutes) {
        this.isMinutes = true;
        this.isHours = false;
        invalidate();
    }


    private class Clock extends View {

        public Clock(Context context) {
            super(context);
        }

        public Clock(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public Clock(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public Clock(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawClock(canvas);
            drawCenterPoint(canvas);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) clockRadius * 2, (int) clockRadius * 2);
        }

        private void drawClock(Canvas canvas) {
            mPaint.setColor(Color.rgb(224, 224, 224));
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            RectF rectF = new RectF(0, 0, 2 * clockRadius, 2 * clockRadius);
            canvas.drawOval(rectF, mPaint);
        }

        private void drawCenterPoint(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            RectF rectF = new RectF(centerX - 6, centerY - 6,
                    centerX + 6, centerY + 6);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private class ClockHand extends View {
        public ClockHand(Context context) {
            super(context);
            initView();
        }

        public ClockHand(Context context, AttributeSet attrs) {
            super(context, attrs);
            initView();
        }

        public ClockHand(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            initView();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ClockHand(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initView();
        }

        public void initView() {
            setBackground(null);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) textRadius + 36, 72);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            drawClockHand(canvas);
            drawTextBackground(canvas);
        }

        private void drawClockHand(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setStrokeWidth(4f);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            canvas.drawLine(0, 36,
                    textRadius, 36, mPaint);
        }

        private void drawTextBackground(Canvas canvas) {
            mPaint.setColor(mPrimaryColor);
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            float textBackgroundRaius = 36f;
            RectF rectF = new RectF(textRadius - textBackgroundRaius, 36 - textBackgroundRaius,
                      textRadius + textBackgroundRaius, 36 + textBackgroundRaius);
            canvas.drawOval(rectF, mPaint);
        }
    }

    private class ClockText extends View {

        public ClockText(Context context) {
            super(context);
        }

        public ClockText(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public ClockText(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public ClockText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            initView();
        }

        private void initView() {
            setBackground(null);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (isHours) {
                drawHours(canvas);
            }
            if (isMinutes) {
                for (Point point : clockTime) {
                    if (point.x != presentTime.x || point.y != presentTime.y) {
                        continue;
                    }
                }
                drawMinutes(canvas);
            }
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension((int) clockRadius * 2, (int) clockRadius * 2);
        }

        private void drawHours(Canvas canvas) {
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setTextSize(40);
            for (int i = 0; i < 12; i++) {
                if (clockTime.get(i).x == presentTime.x && clockTime.get(i).y == presentTime.y) {
                    if (isDarkTheme) {
                        mPaint.setColor(Color.rgb(0, 0, 0));
                    } else {
                        mPaint.setColor(Color.rgb(0, 0, 0));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                } else {
                    if (isDarkTheme) {
                        mPaint.setColor(Color.rgb(0, 0, 0));
                    } else {
                        mPaint.setColor(Color.rgb(0, 0, 0));
                    }
                    canvas.drawText(String.valueOf(i + 1), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                }
            }
        }

        private void drawMinutes(Canvas canvas) {
            mPaint.setAntiAlias(true);
            mPaint.setDither(true);
            mPaint.setTextSize(40);
            for (int i = 0; i < 12; i++) {
                if (clockTime.get(i).x == presentTime.x && clockTime.get(i).y == presentTime.y) {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                    }
                    canvas.drawText(String.valueOf((i + 1) * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                } else {
                    if (isDarkTheme) {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                    } else {
                        mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                    }
                    canvas.drawText(String.valueOf((i + 1) * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
                }
            }
        }
    }
}
