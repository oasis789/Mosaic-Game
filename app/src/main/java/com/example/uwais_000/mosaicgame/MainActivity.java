package com.example.uwais_000.mosaicgame;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private int gridSize, numberOfPlayers, turnTime, numberOfRounds;
    private LinearLayout container;
    private TextView tvActivePlayer, tvTimeLeft, tvRound;
    private int activePlayer, roundNumber;
    private CountDownTimer timer;
    private Context context;
    private GameMetaData gameMetaData;
    private MosaicView mosaicView;
    private ToggleButton toggleButton;
    private boolean isShadeLighter = false;
    private ArrayList<int []> localGameState = new ArrayList<int []>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        //Get data from intent
        Intent intent = getIntent();
        gridSize = intent.getIntExtra(GameMetaData.GRID_SIZE_KEY, 4);
        numberOfPlayers = intent.getIntExtra(GameMetaData.NUMBER_OF_PLAYERS_KEY, 2);
        turnTime = intent.getIntExtra(GameMetaData.TURN_TIME_KEY, 30);
        numberOfRounds = intent.getIntExtra(GameMetaData.NUMBER_OF_ROUNDS_KEY, 3);

        //Save Game Meta data to cloud
        gameMetaData = new GameMetaData();
        gameMetaData.setNumberOfPlayers(numberOfPlayers);
        gameMetaData.setGridSize(gridSize);
        gameMetaData.setNumberOfRounds(numberOfRounds);
        gameMetaData.setTurnTime(turnTime);
        gameMetaData.setGameFinishedState(false);
        gameMetaData.saveInBackground();

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
                gameStateData.setGameStateData(mosaicView.getClickCounter());
                gameStateData.saveInBackground();
                localGameState.add(mosaicView.getClickCounter());

                activePlayer++;

                if(activePlayer > numberOfPlayers){
                    activePlayer = 1;
                    roundNumber ++;
                }

                if(roundNumber > numberOfRounds){
                    //Finish the game
                    Toast.makeText(getApplicationContext(), "Finished!", Toast.LENGTH_SHORT).show();
                    gameMetaData.setGameFinishedState(true);
                    gameMetaData.saveInBackground();

                    AlertDialog finishDialog = new AlertDialog.Builder(context)
                            .setMessage("The game is now finished!")
                            .setPositiveButton("View Game Summary", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), GameResultActivity.class);
                                    intent.putExtra(GameStateData.GAME_STATE_DATA_KEY, localGameState);
                                    intent.putExtra(GameMetaData.GRID_SIZE_KEY, gridSize);
                                    intent.putExtra(GameMetaData.NUMBER_OF_PLAYERS_KEY, numberOfPlayers);
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
        container = (LinearLayout) findViewById(R.id.llMosaicContainer);
        tvActivePlayer = (TextView) findViewById(R.id.tvActivePlayer);
        tvTimeLeft = (TextView) findViewById(R.id.tvTimeLeft);
        tvRound = (TextView) findViewById(R.id.tvRound);
        mosaicView = new MosaicView(context, gridSize, 16);
        container.addView(mosaicView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        toggleButton = (ToggleButton) findViewById(R.id.switchButton);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShadeLighter = isChecked;
                mosaicView.isLighterShade(isShadeLighter);
            }
        });
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
