package com.master.info_ua.videoannottool.annotation_dessin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.master.info_ua.videoannottool.R;

import java.text.Annotation;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DrawView extends View{

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;

    private int lineColor;

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private boolean onTouchEnable; // vrai si on peut dessiner, faux sinon, à activer uniquement quand l'annotation de dessin est enclenchée
    private Context context;

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        lineColor = Color.RED;

        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.MITER);
        mPaint.setStrokeWidth(4f);



        onTouchEnable = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;
        int currentWidth = screenWidth;
        int currentHeignt = screenHeight;
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            currentWidth = MeasureSpec.getSize(widthMeasureSpec);
        }        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            currentHeignt = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(currentWidth, currentHeignt);
        this.mBitmap = Bitmap.createBitmap(currentWidth, currentHeignt, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(this.mBitmap);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.drawPath(mPath, mPaint);
    }

    private void touch_start(float x, float y) {

        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if(onTouchEnable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        }
        return true;

    }

    public void resetCanvas() {
        mPath.reset();
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void setOnTouchEnable(boolean value){
        onTouchEnable = value;
    }

    public void setColor(int color){
        mPaint.setColor(color);
    }

    public void enregistrer_image(String videoName, float duree, String titre) {

        Annotation nouvelle_annotation;
        String date_annotation;
        String start_time_annotation;

        String nom_image;

        //nouvelle_annotation = new Annotation(videoName,date_annotation,duree,start_time_annotation,titre);
        // new Annotation();
        // setfile_name

        // récuperer le nom de la video


        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");

        nom_image =videoName+"_"+dateFormat.format(new Date())+".png";

        //saveBitmap();
        SaveBitmap.saveBitmapImage(context, mBitmap, nom_image); // ici mettre un nom unique pour chaque
        mPath.reset();
        invalidate();
    }

    public void saveBitmap(){
        this.mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
      //  mCanvas = new Canvas(this.mBitmap);
        mCanvas.drawPath(mPath, mPaint);
    }

}
