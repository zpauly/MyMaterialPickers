package com.zpauly.pickers.TimePickers;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.zpauly.pickers.utils.ColorUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by root on 16-4-16.
 */
public class TimePicker extends View {
    private Context mContext;

    private boolean isDarkTheme;

    private int mPrimaryColor;
    private final int mHorizontalPadding = 24;
    private float clockRadius;
    private float centerX;
    private float centerY;
    private boolean isHours = true;
    private boolean isMinutes = false;
    private Point presentTime;
    private int currentHour;
    private int currentMinute;
    private List<Point> clockTime = new ArrayList<>();

    private int mScreeWidth;
    private int mScreeHeight;

    private Paint mPaint;
    private Path mPath;

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
    }

    private void initArguments() {
        fetchPrimaryColor();

        Date date = new Date();
        currentHour = date.getHours();
        currentMinute = date.getMinutes();

        float textRadius = clockRadius - mHorizontalPadding;
        final double oneAngle = 30;
        double totalAngle = 0;
        if (isHours)
            presentTime = new Point((int) (textRadius * Math.cos(currentHour * 30) + centerX),
                    (int) (textRadius * Math.sin(currentHour * 30) + centerY));
        if (isMinutes)
            presentTime = new Point((int) (textRadius * Math.cos(currentHour * 6) + centerX),
                    (int) (textRadius * Math.sin(currentHour * 6) + centerY));
        for (int i = 1; i >= 12; i++) {
            totalAngle += oneAngle;
            float textX = (float) (textRadius * Math.cos(totalAngle));
            float textY = (float) (textRadius * Math.sin(totalAngle));
            float x = centerX + textX;
            float y = centerY + textY;
            Point point = new Point((int) x, (int) y);
            clockTime.add(point);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mScreeWidth - 2 * mHorizontalPadding;
        int height = width;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawClock(canvas);
        drawCenterPoint(canvas);
        if (isHours) {
            drawTextBackground(canvas, presentTime);
            drawHours(canvas);
        }
        if (isMinutes) {
            drawTextBackground(canvas, presentTime);
            drawMinutes(canvas);
        }
        drawClockHand(canvas, new Point((int) centerX, (int) centerY), presentTime);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Point touchTime = null;
        isClickClockTime(event, touchTime);
        return true;
    }

    private boolean isClickClockTime(MotionEvent event, Point touchTime) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                for (Point point : clockTime) {
                    if (event.getX() >= (point.x - 36) && event.getX() >= (point.x + 36)
                            && event.getY() >= (point.y - 36) && event.getY() <= (point.y + 36)) {
                        touchTime = point;
                    }
                }
                break;
            case MotionEvent.ACTION_UP :
                if (touchTime != null
                        && event.getX() >= (touchTime.x - 36) && event.getX() >= (touchTime.x + 36)
                        && event.getY() >= (touchTime.y - 36) && event.getY() <= (touchTime.y + 36)) {
                    presentTime = touchTime;
                    invalidate();
                    touchTime = null;
                    return true;
                } else {
                    touchTime = null;
                    break;
                }
            default :
                break;
        }
        return false;
    }

    private boolean isMoveClockTime(MotionEvent event, Point touchTime) {
        boolean flag = false;
        float previousX = 0;
        float previousY = 0;
        float lastX = 0;
        float lastY = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                if (event.getX() >= (presentTime.x - 36) && event.getX() >= (presentTime.x + 36)
                        && event.getY() >= (presentTime.y - 36) && event.getY() <= (presentTime.y + 36)) {
                    touchTime = presentTime;
                    previousX = event.getX();
                    previousY = event.getY();
                    flag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE :
                if (flag) {
                    lastX = event.getX();
                    lastY = event.getY();
                    if ()
                }

        }
    }

    private void drawClock(Canvas canvas) {
        mPaint.setColor(Color.rgb(224, 224, 224));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.reset();
        float padding = mHorizontalPadding;
        clockRadius = (mScreeWidth - 4 * mHorizontalPadding) / 2;
        centerX = (mScreeWidth - 2 * mHorizontalPadding) /2;
        centerY = centerX;
        RectF rectF = new RectF(padding, padding, padding + 2 * clockRadius, padding + 2 * clockRadius);
        canvas.drawOval(rectF, mPaint);
    }

    private void drawHours(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        for (int i = 1; i >= 12; i++) {
            if (clockTime.get(i).x == presentTime.x && clockTime.get(i).y == presentTime.y) {
                if (isDarkTheme) {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                } else {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                }
                mPaint.reset();
                canvas.drawText(String.valueOf(i), clockTime.get(i).x, clockTime.get(i).y, mPaint);
            } else {
                if (isDarkTheme) {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                } else {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                }
                mPaint.reset();
                canvas.drawText(String.valueOf(i), clockTime.get(i).x, clockTime.get(i).y, mPaint);
            }
        }
    }

    private void drawMinutes(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        for (int i = 1; i >= 12; i++) {
            if (clockTime.get(i).x == presentTime.x && clockTime.get(i).y == presentTime.y) {
                if (isDarkTheme) {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                } else {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                }
                mPaint.reset();
                canvas.drawText(String.valueOf(i * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
            } else {
                if (isDarkTheme) {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(255, 255, 255), 0.70f));
                } else {
                    mPaint.setColor(ColorUtils.getColorWithAlpha(Color.rgb(0, 0, 0), 0.54f));
                }
                mPaint.reset();
                canvas.drawText(String.valueOf(i * 5), clockTime.get(i).x, clockTime.get(i).y, mPaint);
            }
        }
    }

    private void drawCenterPoint(Canvas canvas) {
        mPaint.setColor(mPrimaryColor);
        mPaint.setStrokeWidth(9f);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.reset();
        canvas.drawPoint(centerX, centerY, mPaint);
    }

    private void drawClockHand(Canvas canvas, Point startPoint, Point endPoint) {
        mPaint.setColor(mPrimaryColor);
        mPaint.setStrokeWidth(3f);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.reset();
        canvas.drawLine(startPoint.x, startPoint.y,
                endPoint.x, endPoint.y, mPaint);
    }

    private void drawTextBackground(Canvas canvas, Point presentTime) {
        boolean flag = false;
        for (Point point : clockTime) {
            if (point.x == presentTime.x && point.y == presentTime.y) {
                flag = true;
                break;
            } else {
                flag = false;
            }
        }
        if (!flag) {
            return;
        }
        mPaint.setColor(mPrimaryColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.reset();
        float textBackgroundRaius = 36f;
        RectF rectF = new RectF(presentTime.x - textBackgroundRaius, presentTime.y - textBackgroundRaius,
                presentTime.x + textBackgroundRaius, presentTime.y + textBackgroundRaius);
        canvas.drawOval(rectF, mPaint);
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

    private void setDarkTheme(boolean darkTheme) {
        this.isDarkTheme = darkTheme;
        invalidate();
    }

    private boolean isDarkTheme() {
        return this.isDarkTheme;
    }

    private void setShowHours(boolean isHours) {
        this.isHours = true;
        this.isMinutes = false;
        invalidate();
    }

    private void setShowMinutes(boolean isMinutes) {
        this.isMinutes = true;
        this.isHours = false;
        invalidate();
    }
}
