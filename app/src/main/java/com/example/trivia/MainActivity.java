package com.example.trivia;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import com.example.trivia.controller.AppController;
import com.example.trivia.data.AnswerListAsyncResponse;
import com.example.trivia.data.Repository;
import com.example.trivia.databinding.ActivityMainBindingImpl;
import com.example.trivia.module.Question;
import com.example.trivia.module.Score;
import com.example.trivia.util.Pref;
import com.google.android.material.snackbar.Snackbar;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBindingImpl binding;
    private int c=0;
    private int ScoreCounter=0;
    private Score score;
    private Pref pref;
    List<Question> questionList= new ArrayList<>();
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        score = new Score();
        pref=new Pref(MainActivity.this);
        c=pref.getState();
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main);
        binding.textView4.setText(MessageFormat.format("Highest : {0}", String.valueOf(pref.getHighest())));
        new Repository().getquestions(questionArrayList ->  {
            questionList.addAll(questionArrayList);


            extracted(questionArrayList);
            binding.textView.setText(questionArrayList.get(c).getAnswer());
        });
        binding.button3.setOnClickListener(view -> {
            getNextQuestion();
        });
        binding.button.setOnClickListener(view ->{
            checkAnswer(true);
            updateQuestion();
        });
        binding.button2.setOnClickListener(view ->{
            checkAnswer(false);
            updateQuestion();
        });
    }

    private void getNextQuestion() {
        if (!questionList.isEmpty()) {
            c = (c + 1) % questionList.size();
            updateQuestion();
            pref.saveHighest(ScoreCounter);
        }
    }

    private void checkAnswer(boolean selectedAnswer) {
        Question currentQuestion = questionList.get(c);
        boolean correctAnswer = currentQuestion.isAnswerTrue();
        int messageId;

        if (selectedAnswer == correctAnswer) {
            messageId = R.string.correct_ans;
            fadeAnimation();
            addpoints();
        } else {
            messageId = R.string.wrong_ans;
            shakeAnimation();
            deduct();
        }

        Snackbar.make(binding.cardView, messageId, Snackbar.LENGTH_SHORT).show();
    }

    @SuppressLint("DefaultLocale")
    private void extracted(ArrayList<Question> questionArrayList) {
        binding.textView2.setText(String.format("Question:%d/%d", c, questionArrayList.size()));
    }
    private void fadeAnimation(){
        AlphaAnimation a = new AlphaAnimation(1.0f,0.0f);
        a.setDuration(300);
        a.setRepeatCount(1);
        a.setRepeatMode(Animation.REVERSE);
        binding.cardView.setAnimation(a);
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_annimation);
        binding.cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.textView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.textView.setTextColor(Color.WHITE);
                getNextQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void updateQuestion() {
        String question= questionList.get(c).getAnswer();
        binding.textView.setText(question);
        extracted((ArrayList<Question>) questionList);
    }
    private void deduct(){
        if(ScoreCounter>0){
            ScoreCounter-=1;
            score.setScore(ScoreCounter);
            binding.textView3.setText(String.valueOf(score.getScore()));
        }
        else{
            ScoreCounter=0;
            score.setScore(ScoreCounter);
            binding.textView3.setText(String.valueOf(score.getScore()));
        }
    }
    private void addpoints(){
        ScoreCounter+=10;
        score.setScore(ScoreCounter);
        binding.textView3.setText(String.valueOf(score.getScore()));

    }

    @Override
    protected void onPause() {
        pref.saveHighest(score.getScore());
        pref.setState(c);
        super.onPause();
    }}