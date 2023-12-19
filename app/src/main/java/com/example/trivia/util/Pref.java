package com.example.trivia.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Pref {
    private static final String HIGHEST_SCORE ="highest" ;
    private static final String STATE ="Trivia_state" ;
    private SharedPreferences preferences ;

    public Pref(Activity context) {
        this.preferences = context.getPreferences(Context.MODE_PRIVATE);

    }
    public void saveHighest(int score){
      int currentScore=score;
      int lastScore= preferences.getInt(HIGHEST_SCORE,0);
      if(currentScore>lastScore){
          preferences.edit().putInt(HIGHEST_SCORE,currentScore).apply();
      }
    }
    public int getHighest(){
        return preferences.getInt(HIGHEST_SCORE,0);
    }
    public void setState(int index){
        preferences.edit().putInt(STATE,index).apply();
    }
    public int getState(){
        return preferences.getInt(STATE,0);
    }
}
