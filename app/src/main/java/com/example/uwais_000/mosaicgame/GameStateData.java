package com.example.uwais_000.mosaicgame;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by uwais_000 on 27/07/2015.
 */
@ParseClassName("GameStateData")
public class GameStateData extends ParseObject{

    public static final String GAME_META_DATA_KEY = "gameMetaData";
    public static final String CURRENT_PLAYER_KEY = "currentPlayer";
    public static final String CURRENT_ROUND_KEY = "currentRound";
    public static final String GAME_STATE_DATA_KEY = "gameStateData";

    public GameStateData(){

    }

    //Getters
    public GameMetaData getGameMetaData(){
        return (GameMetaData) getParseObject(GAME_META_DATA_KEY);
    }

    public int getCurrentPlayer(){
        return getInt(CURRENT_PLAYER_KEY);
    }

    public int getCurrentRound(){
        return getInt(CURRENT_ROUND_KEY);
    }

    public int[] getGameStateData(){
        return toIntArray((List<Integer>) (List<?>) getList(GAME_STATE_DATA_KEY));
    }

    int[] toIntArray(List<Integer> list){
        int[] ret = new int[list.size()];
        for(int i = 0;i < ret.length;i++)
            ret[i] = list.get(i);
        return ret;
    }


    //Setters
    public void setGameMetaData(GameMetaData gameMetaData){
        put(GAME_META_DATA_KEY, gameMetaData);
    }

    public void setCurrentPlayer(int currentPlayer){
        put(CURRENT_PLAYER_KEY, currentPlayer);
    }

    public void setCurrentRound(int currentRound){
        put(CURRENT_ROUND_KEY, currentRound);
    }

    public void setGameStateData(int[] gameStateData){
        addAll(GAME_STATE_DATA_KEY, convertArrayToList(gameStateData));
        }

    private List<Integer> convertArrayToList(int[] numArray){
        List<Integer> intList = new ArrayList<Integer>();
        for (int index = 0; index < numArray.length; index++)
        {
            intList.add(numArray[index]);
        }

        return  intList;
    }


}
