package uk.ac.lims.mosaicgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


public class GameResultActivity extends ActionBarActivity {

    private ArrayList<int []> localGameState;
    private int numberOfPlayers, gridSize;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_result);
        tableLayout = (TableLayout) findViewById(R.id.resultsTableLayout);

        Intent intent = getIntent();
        localGameState = (ArrayList<int []>) intent.getSerializableExtra(GameStateData.GAME_STATE_DATA_KEY);
        gridSize = intent.getIntExtra(GameMetaData.GRID_SIZE_KEY, 4);
        numberOfPlayers = intent.getIntExtra(GameMetaData.NUMBER_OF_PLAYERS_KEY, 2);
        int currentPlayer = 1;
        int currentRound = 1;

        for(int[] clickCounter : localGameState){
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView playerNum = new TextView(this);
            playerNum.setText("Round " + currentRound + " - Player " + currentPlayer);
            playerNum.setGravity(Gravity.CENTER);
            int paddingSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
            playerNum.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            float textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
            playerNum.setTextSize(14);
            playerNum.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            tableRow.addView(playerNum);

            MosaicView mosaicView = new MosaicView(this, gridSize, 16);
            mosaicView.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            mosaicView.setClickCounter(clickCounter);
            mosaicView.setEnabled(false);

            tableRow.addView(mosaicView, 200, 500);
            tableRow.setPadding(10, 10, 10, 10);

            tableLayout.addView(tableRow);


            currentPlayer ++;
            if (currentPlayer > numberOfPlayers){
                currentPlayer = 1;
                currentRound ++;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_result, menu);
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
