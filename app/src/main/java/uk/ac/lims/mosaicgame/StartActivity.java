package uk.ac.lims.mosaicgame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;


public class StartActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener{

    private Spinner playersSpinner, gridSpinner, timeSpinner, roundsSpinner;
    private int[] playerArray, gridArray, timeArray, roundsArray;
    private int playerSelected, gridSelected, timeSelected, roundsSelected;
    private Button playButton;
    private boolean singlePlayerMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        if(getIntent().getIntExtra(GameMetaData.NUMBER_OF_PLAYERS_KEY, 2) == 1){
            singlePlayerMode = true;
        }

        //Integer arrays of the equivalent data used in string arrays to populate spinners
        playerArray = new int[] {2,3,4,5};
        gridArray = new int[] {4,8,12,16,20,30};
        timeArray = new int[] {30,45,60,90,120};
        roundsArray = new int[]{3,5,7,10};

        //Default values of the spinner selections - Do I really need these???
        playerSelected = playerArray[0];
        gridSelected = gridArray[0];
        timeSelected = timeArray[0];
        roundsSelected = roundsArray[0];

        SetupSpinners();

        if(singlePlayerMode){
            LinearLayout llPlayers = (LinearLayout) findViewById(R.id.llPlayers);
            llPlayers.setVisibility(View.GONE);
            playerSelected = 1;
        }

        playButton = (Button) findViewById(R.id.btnPlay);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //On Play Button Clicked
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra(GameMetaData.NUMBER_OF_PLAYERS_KEY, playerSelected);
                intent.putExtra(GameMetaData.GRID_SIZE_KEY, gridSelected);
                intent.putExtra(GameMetaData.TURN_TIME_KEY, timeSelected);
                intent.putExtra(GameMetaData.NUMBER_OF_ROUNDS_KEY, roundsSelected);
                startActivity(intent);
            }
        });
    }

    private void SetupSpinners() {
        playersSpinner = (Spinner) findViewById(R.id.playersSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.players_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playersSpinner.setAdapter(adapter);
        playersSpinner.setOnItemSelectedListener(this);

        gridSpinner = (Spinner) findViewById(R.id.gridSizeSpinner);
        ArrayAdapter<CharSequence> gridAdapter = ArrayAdapter.createFromResource(this, R.array.gridSize_array, android.R.layout.simple_spinner_item);
        gridAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridSpinner.setAdapter(gridAdapter);
        gridSpinner.setOnItemSelectedListener(this);

        timeSpinner = (Spinner) findViewById(R.id.timeSpinner);
        ArrayAdapter<CharSequence> timeAdapter = ArrayAdapter.createFromResource(this, R.array.turnTime_array, android.R.layout.simple_spinner_item);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeSpinner.setOnItemSelectedListener(this);

        roundsSpinner = (Spinner) findViewById(R.id.roundsSpinner);
        ArrayAdapter<CharSequence> roundsAdapter = ArrayAdapter.createFromResource(this, R.array.rounds_array, android.R.layout.simple_spinner_item);
        roundsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roundsSpinner.setAdapter(roundsAdapter);
        roundsSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        switch (spinner.getId()){
            case R.id.playersSpinner:
                playerSelected = playerArray[position];
                break;

            case R.id.gridSizeSpinner:
                gridSelected = gridArray[position];
                break;

            case R.id.timeSpinner:
                timeSelected = timeArray[position];
                break;

            case R.id.roundsSpinner:
                roundsSelected = roundsArray[position];
                Log.v("TAG", String.valueOf(roundsSelected));
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
