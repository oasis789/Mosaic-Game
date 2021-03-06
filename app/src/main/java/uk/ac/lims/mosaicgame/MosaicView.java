package uk.ac.lims.mosaicgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by uwais_000 on 28/07/2015.
 */
public class MosaicView extends View{

    private int gridSize;
    private Context context;
    private int[] colors;
    private int[] clickCounter;
    private boolean isShadeLighter = false;
    private int width, height;
    private String TAG = "Mosaic View";
    private Rect[] tiles;

    public MosaicView(Context context, int gridSize, int noOfGrayScales){
        super(context);
        this.gridSize = gridSize;
        this.context = context;
        calculateColors(noOfGrayScales);
        clickCounter = new int[gridSize*gridSize];
        tiles = new Rect[gridSize*gridSize];
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int tileWidth = Math.round((float) width / gridSize);
        final int tileHeight = Math.round((float) height / gridSize);
        int tileTopCoor = 0;
        int tileLeftCoor = 0;
        for(int i=0; i<gridSize; i++){
            for(int j=0; j<gridSize; j++){
                int index = getIndexOfTile(i,j);
                tiles[index] = new Rect(tileLeftCoor, tileTopCoor, tileLeftCoor + tileWidth, tileTopCoor + tileHeight);
                Paint paint = new Paint();
                paint.setColor(getColor(clickCounter[index]));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(tiles[index], paint);
                tileLeftCoor += tileWidth;
            }
            tileTopCoor += tileHeight;
            tileLeftCoor = 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        Log.v(TAG, "ratio " + (float) h/w);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //Log.v(TAG, "onMeasure Width: " + parentWidth);
        //Log.v(TAG, "onMeasure Height: " + parentHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isClickable()) {
            //Check for single click
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                for (int i = 0; i < gridSize; i++) {
                    for (int j = 0; j < gridSize; j++) {
                        int index = getIndexOfTile(i, j);
                        if (tiles[index].contains(x, y)) {
                            if (isShadeLighter) {
                                //Remove circular behaviour of gray scale
                                //Cannot shade lighter if tile is already white
                                if(clickCounter[index] > 0){
                                    clickCounter[index]--;
                                }
                            } else {
                                //Remove circular behaviour of gray scale
                                //Cannot shade darker if tile is already black
                                if(clickCounter[index] < colors.length - 1){
                                    clickCounter[index]++;
                                }
                            }
                            invalidate();
                            return true;
                        }
                    }
                }
            }
            //TODO: Add functionality for a continuous swipe gesture to fill in the tiles?
        }
        return true;
    }

    private void calculateColors(int noOfGrayScales) {
        colors = new int[noOfGrayScales];
        for(int i=0; i < noOfGrayScales; i++){
            colors[i] = 256 - (i*noOfGrayScales) - 1 ;
        }
        colors[noOfGrayScales - 1 ] = 0;
    }

    private int getIndexOfTile(int i, int j){
        return ((gridSize*i) + j);
    }

    private int getColor(int clickNumber){
        int colorNum = 0;

        //Determine which shade of grey to use based on the number of clicks
        if(clickNumber > colors.length-1){
            colorNum = colors[clickNumber % colors.length];
        }else{
            colorNum = colors[clickNumber];
        }

        return Color.rgb(colorNum, colorNum, colorNum);
    }


    public int[] getClickCounter() {
        return clickCounter;
    }

    public void setClickCounter(int[] clickCounter){
        this.clickCounter = clickCounter;
    }

    public void isLighterShade(boolean isLighterShade){this.isShadeLighter = isLighterShade;}

    public Bitmap getBitmap(){
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }

}
