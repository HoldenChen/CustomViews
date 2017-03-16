package com.holden.customviews.scatch_card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Holden on 17/3/2.
 */
public class SignView extends View {

    Path mPath;
    Paint mPaint;
    Bitmap mBitmap;
    Canvas mCanvas;
    int mHeight;
    int mWidth;

    float mLastX;
    float mLastY;

    public SignView(Context context){
        this(context,null);
    }

    public SignView(Context context,AttributeSet attrs){
        this(context,attrs,0);
    }

    public SignView(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        init();
    }

    private void init(){
        mPath = new Path();
        mPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //初始化画笔
        mPaint.setColor(Color.RED);

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
        mPaint.setStrokeWidth(20);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawPath();
        canvas.drawBitmap(mBitmap,0,0,null);
    }

    private void drawPath(){
        mCanvas.drawPath(mPath,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = event.getX();
                mLastY = event.getY();
                mPath.moveTo(mLastX,mLastY);
                break;
            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(event.getX()-mLastX);
                float dy = Math.abs(event.getX()-mLastY);
                if(dx>3||dy>3){
                    mPath.lineTo(event.getX(),event.getY());
                }
                mLastX = event.getX();
                mLastY = event.getY();
                break;
        }
        invalidate();
        return true;
    }
}

