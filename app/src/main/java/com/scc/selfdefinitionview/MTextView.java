package com.scc.selfdefinitionview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class MTextView extends LinearLayout {
    private String text;
    private int color;
    private int textSize = 15;
    private Paint mPaint;

    public MTextView(Context context) {
        this(context, null);
    }

    public MTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MTextView);
        text = typedArray.getString(R.styleable.MTextView_text);
        color = typedArray.getColor(R.styleable.MTextView_textColor, Color.BLACK);
        textSize = typedArray.getDimensionPixelSize(R.styleable.MTextView_textSize, textSize);
        //获取属性值
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(textSize);
        mPaint.setColor(color);

        //  针对viewgroup初始化不会onDraw，默认给一个背景
        // setBackgroundColor(Color.TRANSPARENT);
        //针对viewgroup初始化不会onDraw
        setWillNotDraw(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int width = 0;
        if (widthMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            width = bounds.width() + getPaddingLeft() + getPaddingRight();
        }

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int height = 0;
        if (heightMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds(text, 0, text.length(), bounds);
            height = bounds.height() + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //针对viewgroup初始化不会onDraw
        super.dispatchDraw(canvas);
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
//        int baseline = getHeight() - fontMetricsInt.bottom;
        //top 负值 bottom 正直
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseline = getHeight() / 2 + dy;
        int x = getPaddingLeft();
        canvas.drawText(text, x, baseline, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed,left,top,right,bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return super.onTouchEvent(event);
    }

    public void setText(String text){

        invalidate();
    }
}
