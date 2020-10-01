package com.scc.selfdefinitionview;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;

public class QQStepView extends View {
    private int innerColor = Color.parseColor("#8A2BE2");
    private int outterColor = Color.parseColor("#FF69B4");
    private int borderWidth = 20;   //20px
    private int stepTextColor = Color.RED;
    private int stepTextSize = 15; //15px
    private int stepText = 1;
    private int stepMaxText;

    private Paint mOutPaint;
    private Paint mInPaint;
    private Paint mTextPaint;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        stepText = typedArray.getInt(R.styleable.QQStepView_stepText, 1);
        stepTextColor = typedArray.getColor(R.styleable.QQStepView_stepTextColor, stepTextColor);
        stepTextSize = typedArray.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, stepTextSize);
        borderWidth = typedArray.getDimensionPixelOffset(R.styleable.QQStepView_borderWidth, borderWidth);
        innerColor = typedArray.getColor(R.styleable.QQStepView_innerColor, innerColor);
        outterColor = typedArray.getColor(R.styleable.QQStepView_outterColor, outterColor);
        stepMaxText = typedArray.getInt(R.styleable.QQStepView_stepMaxText, stepText);
        typedArray.recycle();

        mOutPaint = new Paint();
        mOutPaint.setColor(outterColor);
        mOutPaint.setStrokeWidth(borderWidth);
        mOutPaint.setStrokeCap(Paint.Cap.ROUND);
        mOutPaint.setStyle(Paint.Style.STROKE);
        mOutPaint.setAntiAlias(true);

        mInPaint = new Paint();
        mInPaint.setColor(innerColor);
        mInPaint.setStrokeWidth(borderWidth);
        mInPaint.setStrokeCap(Paint.Cap.ROUND);
        mInPaint.setStyle(Paint.Style.STROKE);
        mInPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setColor(stepTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(stepTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getMode(heightMeasureSpec);
        int side = width > height ? height : width;
        setMeasuredDimension(side, side);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rect = new RectF(borderWidth / 2, borderWidth / 2, getHeight() - borderWidth / 2, getWidth() - borderWidth / 2);
        canvas.drawArc(rect, 135, 270, false, mOutPaint);
        float sweepAngle = (float) stepText / stepMaxText;
        canvas.drawArc(rect, 135, sweepAngle * 270, false, mInPaint);

        Rect bounds = new Rect();
        mTextPaint.getTextBounds(String.valueOf(stepText), 0, String.valueOf(stepText).length(), bounds);
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        canvas.drawText(String.valueOf(stepText), getWidth() / 2 - bounds.width() / 2, getHeight() / 2 + dy, mTextPaint);
    }

    public synchronized void setStepText(int stepText) {
        this.stepText = stepText;
        invalidate();
    }

    public synchronized void start(){
        this.start(stepText,stepMaxText);
    }

    public synchronized void start(int stepText,int stepMaxText){
        this.stepText = stepText;
        this.stepMaxText = stepMaxText;

        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,stepText);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int step = (int)animation.getAnimatedValue();
                setStepText(step);
            }
        });
        valueAnimator.start();
    }
}
