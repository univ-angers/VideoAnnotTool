package com.master.info_ua.videoannottool.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.master.info_ua.videoannottool.util.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DrawView extends View {

    //Largeur actuelle
    private int currentWidth;
    //Hauteur actuelle
    private int currentHeignt;
    private Bitmap mBitmap;
    //Toile
    private Canvas mCanvas;
    //Position actuelle sur la toile où l'on souhaite dessiner
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    //Couleur de la ligne
    private int lineColor;
    //Coordonnées du dessin (point de départ?)
    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;
    //Vrai si on peut dessiner, à activer uniquement quand l'annotation de dessin est enclenchée
    private boolean onTouchEnable;
    private Context context;

    /*
     * Constructeurs
     */
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
        //Largeur de l'écran
        int screenWidth = displayMetrics.widthPixels;
        //Hauteur de l'écran
        int screenHeight = displayMetrics.heightPixels;
        currentWidth = screenWidth;
        currentHeignt = screenHeight;
        if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            currentWidth = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.UNSPECIFIED) {
            currentHeignt = MeasureSpec.getSize(heightMeasureSpec);
        }
        //Définition des dimensions
        setMeasuredDimension(currentWidth, currentHeignt);
    }

    //Méthode appelée lorsque la taille de l'écran change (plein écran ?)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    //Méthode permettant de dessiner
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        mCanvas.drawPath(mPath, mPaint);
        canvas.drawPath(mPath, mPaint);
    }

    //
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

    //Méthode appelée lorsque l'on touche l'écran
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (onTouchEnable) {
            switch (event.getAction()) {
                //Lors du premier contact avec l'écran
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                //Lors d'un contact "permanent" avec l'écran
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                //Lors du dernier contact avec l'écran
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
        }
        return true;
    }

    //Réinitialise la toile
    public void resetCanvas() {
        mBitmap.eraseColor(android.graphics.Color.TRANSPARENT);
        mPath.reset();
        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void setOnTouchEnable(boolean value) {
        onTouchEnable = value;
    }

    //Définit la couleur du dessin
    public void setColor(int color) {
        mPaint.setXfermode(null);
        mPaint.setStrokeWidth(4f);
        mPaint.setColor(color);
    }

    //Permet de desiner avec un gomme pour effacer
    public void setErase(){
        mPaint.setColor(Color.TRANSPARENT);
        mPaint.setAlpha(0);
        mPaint.setStrokeWidth(50);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setAntiAlias(true);
    }

    //Enregistre l'image
    public String enregistrer_image(String path, String videoName) {
        //Définition du format de la date
        final SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy-HHmmss");
        //Définition du chemin du fichier
        String drawFileName = videoName + "_" + dateFormat.format(new Date()) + ".png";
        //Sauvegarde du dession : attention, mettre un nom unique pour chaque dessin
        Util.saveBitmapImage(context, mBitmap, path, drawFileName);
        mPath.reset();
        invalidate();
        return drawFileName;
    }

    public void setmBitmap(Bitmap bitmap) {
        mCanvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
    }
}