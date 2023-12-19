package com.example.trivia.data;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.trivia.controller.AppController;
import com.example.trivia.module.Question;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Repository {

    //https://raw.githubusercontent.com/4skinSkywalker/aptitude-tests/main/probability.json
    String url = "https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    ArrayList<Question> questionArrayList = new ArrayList<>();

    public List<Question> getquestions(final AnswerListAsyncResponse callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            for (int i = 0; i < response.length(); i++) {
                try {
                    String questionText = response.getJSONArray(i).getString(0);
                    boolean answer = response.getJSONArray(i).getBoolean(1);
                    Question question = new Question(questionText, answer);
                    questionArrayList.add(question);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (callback != null) {
                callback.processfinished(questionArrayList);
            }
        }, error -> {
            // Handle error
        });

        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;
    }
}