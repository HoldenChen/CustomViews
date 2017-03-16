package com.holden.customviews.scatch_card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Holden on 17/3/2.
 */
public class ScratchCardView extends View {

    Path mPath;
    Paint mPaint;
    Paint mRoundRecPaint;
    Bitmap mCoverBitmap;
    Bitmap mBackBitmap;
    int mWidth;
    int mHeight;
    Canvas mCanvas;
    float mLastX;
    float mLastY;
    int mBackBitmapRef;
    int mCoverBitmapRef;
    boolean mStringOrmBitmap;
    int mTextSize;
    String mTextColorString;

    public ScratchCardView(Context context, AttributeSet attrs, int defstyle){
        super(context, attrs, defstyle);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ScratchCardView,0,0);
        try{
            mBackBitmapRef = ta.getResourceId(R.styleable.ScratchCardView_backBitmap,0);
            mCoverBitmapRef = ta.getResourceId(R.styleable.ScratchCardView_coverBitmap,0);
            textString = ta.getString(R.styleable.ScratchCardView_backString);
            mStringOrmBitmap = ta.getBoolean(R.styleable.ScratchCardView_stringOrBitmap, false);
            mTextSize = ta.getInteger(R.styleable.ScratchCardView_textSize, 22);
            mTextColorString = ta.getString(R.styleable.ScratchCardView_textColor);
        }finally {
            ta.recycle();
        }
        init();
    }

    public ScratchCardView(Context context,AttributeSet attrs){
        this(context, attrs, 0);

    }

    public ScratchCardView(Context context){
        this(context, null);
    }

    private void init(){
        mPaint = new Paint();
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mStringOrmBitmap){
            canvas.drawText(textString, getWidth() / 2 - mTextRect.width() / 2,
                    getHeight() / 2 + mTextRect.height() / 2, mBackTextPaint);
        }else{
            canvas.drawBitmap(mBackBitmap,0,0,null);
        }
        //刮开部分大于70% 则移除覆盖区
        if(!isComplete){
            drawPath();
            canvas.drawBitmap(mCoverBitmap,0,0,null);
        }
    }

    private void drawPath(){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        mCanvas.drawPath(mPath,mPaint);
    }

    public Bitmap resizeBitmap(Bitmap bitmap,int w,int h)
    {
        if(bitmap!=null)
        {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            int newWidth = w;
            int newHeight = h;
            float scaleWight = ((float) newWidth)/width;
            float scaleHeight = ((float)newHeight)/height;
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWight, scaleHeight);
            Bitmap res = Bitmap.createBitmap(bitmap, 0,0,width, height, matrix, true);
            return res;
        }
        else{
            return null;
        }
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        setUpBackTextPaint();
        setUpPaint();
        setUpRoundPaint();
        mBackBitmap = createRecBackBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), mBackBitmapRef), mWidth, mHeight));
        mCoverBitmap = createRecCoverBitmap(resizeBitmap(BitmapFactory.decodeResource(getResources(), mCoverBitmapRef),mWidth,mHeight));//createBitmap(BitmapFactory.decodeResource(getResources(), mCoverBitmapRef));
    }

    public Bitmap createRecBackBitmap(Bitmap res){
        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(target);
        RectF rectf = new RectF(0,0,target.getWidth(),target.getHeight());
        Paint recPaint = new Paint();
        recPaint.setAntiAlias(true);
        recPaint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectf, 100, 100, recPaint);
        recPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(res,0,0, recPaint);
        return target;
    }

    public Bitmap createRecCoverBitmap(Bitmap res){

        Bitmap target = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas  = new Canvas(target);
        RectF rectf = new RectF(0,0,target.getWidth(),target.getHeight());
        Paint recPaint = new Paint();
        recPaint.setAntiAlias(true);
        recPaint.setStyle(Paint.Style.FILL);
        mCanvas.drawRoundRect(rectf, 100, 100, recPaint);
        recPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        mCanvas.drawBitmap(res,0,0, recPaint);
        return target;
    }

    private Paint mBackTextPaint = new Paint();
    private Rect mTextRect = new Rect();
    private String textString = "";

    private void setUpBackTextPaint() {
        mBackTextPaint.setColor(Color.parseColor(mTextColorString));
        mBackTextPaint.setStyle(Paint.Style.FILL);
        mBackTextPaint.setTextScaleX(2);
        mBackTextPaint.setTextSize(mTextSize);
        mBackTextPaint.getTextBounds(textString,0,textString.length(),mTextRect);
    }

    private void setUpPaint(){
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND); // 圆角
        mPaint.setStrokeCap(Paint.Cap.ROUND); // 圆角
        mPaint.setStrokeWidth(80);
    }

    private void setUpRoundPaint(){
        mRoundRecPaint = new Paint();
        mRoundRecPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();

        switch (action){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mPath.moveTo(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(x-mLastX);
                float dy = Math.abs(y-mLastY);
                if(dx>3||dy>3){
                    mPath.lineTo(x,y);
                }
                mLastY = y;
                mLastX = x;
                break;
            case MotionEvent.ACTION_UP:
                new Thread(mAutoClearRunnable).start();
                break;
        }
        invalidate();
         return true;
    }


    boolean isComplete;
    private Runnable mAutoClearRunnable = new Runnable(){
        int [] mPixels;
        @Override
       public void run(){

            int width = getWidth();
            int height = getHeight();
            float totalArea = width*height;
            float wipedArea = 0;
            mPixels = new int[width*height];
            Bitmap bitmap = mCoverBitmap;
            bitmap.getPixels(mPixels,0,width,0,0,width,height);

            for(int i = 0;i<height;i++){

                for(int j = 0;j<width;j++){
                    int index = i*width+j;
                    if(mPixels[index] == 0){
                        wipedArea++;
                    }
                    if(wipedArea>0&&totalArea>0&&wipedArea/totalArea>0.7){
                        isComplete = true;
                        postInvalidate();
                        break;
                    }
                }
            }
        }
    };
}
