package com.example.uwais_000.mosaicgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    int gridSize, players, turnTime, rounds;
    TableLayout table;
    RelativeLayout root;
    RelativeLayout header;
    TextView tvActivePlayer, tvTimeLeft, tvRound;
    int[] colors;
    int[] clickCounter;
    int headerBottomPos;
    int activePlayer, roundNumber;
    CountDownTimer timer;
    Context context;
    GameMetaData gameMetaData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Get data from intent
        Intent intent = getIntent();
        gridSize = intent.getIntExtra("GRID", 4);
        players = intent.getIntExtra("PLAYERS", 1);
        turnTime = intent.getIntExtra("TIME", 30);
        turnTime = 10;
        rounds = intent.getIntExtra("ROUNDS", 3);


        //Save Game Meta data to cloud
        gameMetaData = new GameMetaData();
        gameMetaData.setNumberOfPlayers(players);
        gameMetaData.setGridSize(gridSize);
        gameMetaData.setNumberOfRounds(rounds);
        gameMetaData.setTurnTime(turnTime);
        gameMetaData.setGameFinishedState(false);
        gameMetaData.saveInBackground();

        //RGB value of the colors: White, Light Grey, Dark Grey and Black
        colors = new int[] {255,160,96,0};
        //Keeps track of the number of clicks on each tile
        clickCounter = new int[gridSize * gridSize];

        createLayout();



        timer = new CountDownTimer(turnTime*1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                // Log.v("TAG", "seconds remaining: " + millisUntilFinished / 1000);
                tvTimeLeft.setText(millisUntilFinished / 1000 + " Seconds");
            }

            @Override
            public void onFinish() {
                tvTimeLeft.setText("0 Seconds");
                Toast.makeText(getApplicationContext(), "Times Up!!", Toast.LENGTH_SHORT).show();
                //Save current game state in the cloud
                GameStateData gameStateData = new GameStateData();
                gameStateData.setCurrentPlayer(activePlayer);
                gameStateData.setCurrentRound(roundNumber);
                gameStateData.setGameMetaData(gameMetaData);
                gameStateData.setGameStateData(clickCounter);
                gameStateData.saveInBackground();


                activePlayer++;

                if(activePlayer > players){
                    activePlayer = 1;
                    roundNumber ++;
                }

                if(roundNumber > rounds){
                    //Finish the game
                    Toast.makeText(getApplicationContext(), "Finished!", Toast.LENGTH_SHORT).show();
                    gameMetaData.setGameFinishedState(true);
                    gameMetaData.saveInBackground();

                    AlertDialog finishDialog = new AlertDialog.Builder(context)
                            .setMessage("The game is now finished!")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).setCancelable(false).create();
                    finishDialog.show();
                    timer.cancel();
                }else{
                    //Dialog asking user to pass device
                    AlertDialog dialog = new AlertDialog.Builder(context)
                            .setMessage("Your turn has now finished. Please pass the device to Player " + activePlayer)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    tvActivePlayer.setText("Player " + activePlayer);
                                    if (activePlayer == 1){
                                        Toast.makeText(getApplicationContext(), "Round " + roundNumber, Toast.LENGTH_SHORT).show();
                                        tvRound.setText("Round " + roundNumber);
                                    }
                                    tvTimeLeft.setText(turnTime + " Seconds");
                                    //Play
                                    playGame();
                                }
                            }).setCancelable(false).create();
                    dialog.show();
                }
            }
        };

        //First Player is player one
        activePlayer = 1;
        tvActivePlayer.setText("Player " + activePlayer);
        //First Round is round one
        roundNumber = 1;
        tvRound.setText("Round " + roundNumber);
        tvTimeLeft.setText(turnTime + " Seconds");
        playGame();
    }

    private void playGame(){
        //Ready dialog for next player
        AlertDialog dialog2 = new AlertDialog.Builder(context)
                .setMessage("Player " + activePlayer + "! Are you ready?")
                .setPositiveButton("Let's Play!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //Play
                        timer.start();
                    }
                }).setCancelable(false).create();
        dialog2.show();
    }


    private void createLayout() {
        table = (TableLayout) findViewById(R.id.tableLayout);
        root = (RelativeLayout) findViewById(R.id.root);
        header = (RelativeLayout) findViewById(R.id.llHeader);
        tvActivePlayer = (TextView) findViewById(R.id.tvActivePlayer);
        tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
        tvRound = (TextView) findViewById(R.id.tvRound);

        header.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                header.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                drawMosaicView(table.getHeight(), table.getWidth());
            }
        });
    }

    private void drawMosaicView(int screenHeight, int screenWidth) {
        Log.v("TAG", String.valueOf(screenWidth) + " width");
        Log.v("TAG", String.valueOf(screenHeight) + " height");

        //Individual height and width of the tiles
        final int tileWidth = Math.round((float) screenWidth / gridSize);
        final int tileHeight = Math.round((float) screenHeight / gridSize);

        for(int i=0; i<gridSize; i++){
            //Create TableRow as parent for this row
            TableRow tableRow = new TableRow(this);

            for(int j=0; j< gridSize; j++){
                //Add tiles to the current row
                final ImageView tile = new ImageView(this);
                tile.setId(((gridSize*i) + j));
                Log.v("TAG", String.valueOf(tileWidth) + " tile width");
                Log.v("TAG", String.valueOf(tileHeight) + " tile height");
                Bitmap bmp = Bitmap.createBitmap(tileWidth,tileHeight, Bitmap.Config.RGB_565);
                //Set initial bitmap and color it white
                tile.setImageBitmap(colorBitmap(bmp,0));
                tile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bitmap bmp = ((BitmapDrawable)tile.getDrawable()).getBitmap();
                        int id = v.getId();
                        clickCounter[id]++;
                        tile.setImageBitmap(colorBitmap(bmp,clickCounter[id]));
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
        paint.setColor(Color.rgb(colorNum,colorNum,colorNum));
        canvas.drawRect(r, paint);

        //Draw Border
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        canvas.drawRect(r, paint);

        return bmp;
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
