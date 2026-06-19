package com.example.saunatimerapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;
import android.os.SystemClock; // SystemClock for elapsedRealtime

public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private Button startButton;
    private Handler handler;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.startButton);
        handler = new Handler();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    startTimer();
                } else {
                    stopTimer();
                }
            }
        });
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            long updatedTime = timeSwapBuff + timeInMilliseconds;

            int seconds = (int) (updatedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (updatedTime % 1000);

            timerTextView.setText(String.format("%02d:%02d.%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 0); // Update as fast as possible
        }
    };

    private void startTimer() {
        startTime = SystemClock.uptimeMillis();
        handler.postDelayed(updateTimerThread, 0);
        startButton.setText("STOP");
        isRunning = true;
    }

    private void stopTimer() {
        timeSwapBuff += timeInMilliseconds;
        handler.removeCallbacks(updateTimerThread);
        startButton.setText("START");
        isRunning = false;
        // Optionally reset timer: timerTextView.setText("00:00.000"); timeInMilliseconds = 0L; timeSwapBuff = 0L;
    }
}
