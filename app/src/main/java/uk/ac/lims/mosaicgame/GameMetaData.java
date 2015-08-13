package uk.ac.lims.mosaicgame;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by uwais_000 on 27/07/2015.
 */
@ParseClassName("GameMetaData")
public class GameMetaData extends ParseObject{

    public static final String NUMBER_OF_PLAYERS_KEY = "numOfPlayers";
    public static final String GRID_SIZE_KEY = "gridSize";
    public static final String TURN_TIME_KEY = "turnTime";
    public static final String NUMBER_OF_ROUNDS_KEY = "numOfRounds";
    public  static final String IS_GAME_FINISHED_KEY = "isGameFinished";

    public GameMetaData(){
    }

    //Getters
    public int getNumberOfPlayers(){
        return getInt(NUMBER_OF_PLAYERS_KEY);
    }

    public int getGridSize(){
        return getInt(GRID_SIZE_KEY);
    }

    public int getTurnTime(){
        return getInt(TURN_TIME_KEY);
    }

    public int getNumberOfRounds(){
        return getInt(NUMBER_OF_ROUNDS_KEY);
    }

    public boolean isGameFinished(){
        return getBoolean(IS_GAME_FINISHED_KEY);
    }


    //Setters
    public void setNumberOfPlayers(int numberOfPlayers){
        put(NUMBER_OF_PLAYERS_KEY, numberOfPlayers);
    }

    public void setGridSize(int gridSize){
        put(GRID_SIZE_KEY, gridSize);
    }

    public void setTurnTime(int turnTime){
        put(TURN_TIME_KEY, turnTime);
    }

    public void setNumberOfRounds(int numberOfRounds){
        put(NUMBER_OF_ROUNDS_KEY, numberOfRounds);
    }

    public void setGameFinishedState(boolean isGameFinished){
        put(IS_GAME_FINISHED_KEY, isGameFinished);
    }


}
