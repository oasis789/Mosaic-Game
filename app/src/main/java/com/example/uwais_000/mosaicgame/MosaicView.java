package com.example.uwais_000.mosaicgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by uwais_000 on 28/07/2015.
 */
public class MosaicView {

    private int gridSize;
    private Context context;
    private int[] colors;
    private int[] clickCounter;

    public MosaicView(Context context, int gridSize, int noOfGrayScales){
        this.gridSize = gridSize;
        this.context = context;
        calculateColors(noOfGrayScales);
        clickCounter = new int[gridSize*gridSize];
    }

    private void calculateColors(int noOfGrayScales) {
        colors = new int[noOfGrayScales];
        for(int i=0; i < noOfGrayScales; i++){
            colors[i] = 256 - (i*noOfGrayScales) - 1 ;
        }
        colors[noOfGrayScales - 1 ] = 0;
    }

    public void drawMosaicView(TableLayout table, int screenHeight, int screenWidth){
        //Individual height and width of the tiles
        final int tileWidth = Math.round((float) screenWidth / gridSize);
        final int tileHeight = Math.round((float) screenHeight / gridSize);

        for(int i=0; i<gridSize; i++){
            //Create TableRow as parent for this row
            TableRow tableRow = new TableRow(context);

            for(int j=0; j< gridSize; j++){
                //Add tiles to the current row
                final ImageView tile = new ImageView(context);
                tile.setId(((gridSize*i) + j));
                Bitmap bmp = Bitmap.createBitmap(tileWidth,tileHeight, Bitmap.Config.RGB_565);
                //Set initial bitmap and color it white
                tile.setImageBitmap(colorBitmap(bmp, 0));

                //On Click make the tile one shade darker
                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bmp = ((BitmapDrawable) tile.getDrawable()).getBitmap();
                        int id = v.getId();
                        clickCounter[id]++;
                        tile.setImageBitmap(colorBitmap(bmp, clickCounter[id]));
                    }
                });

                //On long click make the tile one shade lighter
                tile.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Bitmap bmp = ((BitmapDrawable) tile.getDrawable()).getBitmap();
                        int id = v.getId();
                        clickCounter[id] += (colors.length - 1);
                        tile.setImageBitmap(colorBitmap(bmp, clickCounter[id]));
                        return true;
                    }
                });

                tableRow.addView(tile);
            }
            table.addView(tableRow);
        }

        Log.v("TAG", String.valueOf(table.getMeasuredWidth()) + " table width");
        Log.v("TAG", String.valueOf(table.getMeasuredHeight()) + " table height");
    }

    private Bitmap colorBitmap(Bitmap bmp, int clickNumber){
        Rect r = new Rect(0, 0, bmp.getWidth(), bmp.getHeight());
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();
        int colorNum = 0;

        //Determine which shade of grey to use based on the number of clicks
        if(clickNumber > colors.length-1){
            colorNum = colors[clickNumber % colors.length];
        }else{
            colorNum = colors[clickNumber];
        }

        //Draw fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.rgb(colorNum, colorNum, colorNum));
        canvas.drawRect(r, paint);

        //Draw Border
        /**paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawRect(r, paint);*/

        return bmp;
    }


    public int[] getClickCounter() {
        return clickCounter;
    }
}
