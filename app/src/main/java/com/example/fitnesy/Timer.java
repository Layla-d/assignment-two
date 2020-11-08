package com.example.fitnesy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class Timer extends AppCompatActivity {
    private EditText editTextInput;
    private TextView textViewTimerCountDown;
    private Button setButton;
    private Button StartAndPauseButton;
    private Button resetButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning;
    private long startTimeInMilliSeconds;
    private long timeLeftInMilliSeconds;
    private long endTime;
    public TextView nameReceived;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        Intent intent = getIntent();
        nameReceived = findViewById(R.id.textView);
        String msg = intent.getStringExtra("data");
        nameReceived.setText("Welcome "  + "\n"+ msg + " !");
        editTextInput = findViewById(R.id.edit_text_input);
        textViewTimerCountDown = findViewById(R.id.text_view_countdown);
        setButton = findViewById(R.id.button_set);
        StartAndPauseButton = findViewById(R.id.button_start_pause);
        resetButton = findViewById(R.id.button_reset);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = editTextInput.getText().toString();
                if (input.length() == 0)
                {
                    Toast.makeText(Timer.this, "Please enter a number", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0)
                {
                    Toast.makeText(Timer.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                editTextInput.setText("");
            }
        });
        StartAndPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning)
                {
                    pauseTimer();
                } else
                    {
                    startTimer();
                }
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }
    private void setTime(long milliseconds) {
        startTimeInMilliSeconds = milliseconds;
        resetTimer();
    }
    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMilliSeconds;
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSeconds = millisUntilFinished;
                changeTimerCountDownText();
            }
            @Override
            public void onFinish() {
                timerRunning = false;
                changeTimer();
            }
        }.start();
        timerRunning = true;
        changeTimer();
    }
    private void pauseTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        changeTimer();
    }
    private void resetTimer() {
        timeLeftInMilliSeconds = startTimeInMilliSeconds;
        changeTimerCountDownText();
        changeTimer();
    }
    private void changeTimerCountDownText() {

        int hours = (int) (timeLeftInMilliSeconds / 1000) / 3600;
        int minutes = (int) ((timeLeftInMilliSeconds / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMilliSeconds / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        textViewTimerCountDown.setText(timeLeftFormatted);
    }
    private void changeTimer() {
        if (timerRunning) {
//            editTextInput.setVisibility(View.INVISIBLE);
            setButton.setVisibility(View.INVISIBLE);
//            resetButton.setVisibility(View.INVISIBLE);
            StartAndPauseButton.setText("Pause");
        } else {
            editTextInput.setVisibility(View.VISIBLE);
            setButton.setVisibility(View.VISIBLE);
            StartAndPauseButton.setText("Start");
            if (timeLeftInMilliSeconds < 1000) {
                StartAndPauseButton.setVisibility(View.INVISIBLE);
            } else {
                StartAndPauseButton.setVisibility(View.VISIBLE);
            }
            if (timeLeftInMilliSeconds < startTimeInMilliSeconds) {
                resetButton.setVisibility(View.VISIBLE);
            } else {
                resetButton.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", startTimeInMilliSeconds);
        editor.putLong("millisLeft", timeLeftInMilliSeconds);
        editor.putBoolean("timerRunning", timerRunning);
        editor.putLong("endTime", endTime);
        editor.apply();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        startTimeInMilliSeconds = prefs.getLong("startTimeInMillis", 600000);
        timeLeftInMilliSeconds = prefs.getLong("millisLeft", startTimeInMilliSeconds);
        timerRunning = prefs.getBoolean("timerRunning", false);
        changeTimerCountDownText();
        changeTimer();
        if (timerRunning) {
            endTime = prefs.getLong("endTime", 0);
            timeLeftInMilliSeconds = endTime - System.currentTimeMillis();
            if (timeLeftInMilliSeconds < 0) {
                timeLeftInMilliSeconds = 0;
                timerRunning = false;
                changeTimerCountDownText();
                changeTimer();
            } else {
                startTimer();
            }
        }

    }
}