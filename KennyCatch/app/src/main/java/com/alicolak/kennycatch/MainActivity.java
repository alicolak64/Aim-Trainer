package com.alicolak.kennycatch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    Runnable runnable;
    SharedPreferences sharedPreferences;
    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView [] imageViews;
    TextView timeText;
    TextView scoreText;
    TextView topScoreText;
    Button startButton;
    int score;
    int topScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        timeText = findViewById(R.id.timeText);
        scoreText = findViewById(R.id.scoreText);
        topScoreText = findViewById(R.id.topScoreText);
        startButton = findViewById(R.id.startButton);
        imageViews = new ImageView [] {imageView1,imageView2,imageView3,imageView4,imageView5,imageView6,imageView7,imageView8,imageView9};
        sharedPreferences = this.getSharedPreferences("com.alicolak.rtecatch",MODE_PRIVATE);
        score = 0;
        topScore = 0;

        hideImages();

        int tempScore = sharedPreferences.getInt("topScore",0);
        topScoreText.setText("Top Score : "+tempScore);
    }

    public void increaseScore(View view) {
        score++;
        scoreText.setText("Score : "+score);
    }

    public void hideImages () {
        handler = new Handler();

        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView imageView : imageViews) {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        };
        handler.post(runnable);
    }

    public void showImage () {
        handler = new Handler();


        runnable = new Runnable() {
            int temp = -1;
            @Override
            public void run() {


                for (ImageView imageView : imageViews) {
                    imageView.setVisibility(View.INVISIBLE);
                }
                Random random=new Random();
                int i=random.nextInt(9);
                if (temp==i)
                    i = (i%3)+2;
                temp = i;
                imageViews[i].setVisibility(View.VISIBLE);
                scoreText.setText("Score : "+score);

                handler.postDelayed(runnable,250);
            }
        };
        handler.post(runnable);
    }

    public void start (View view) {
        showImage();
        new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeText.setText("Time : "+millisUntilFinished/1000);
                startButton.setEnabled(false);
            }

            @Override
            public void onFinish() {

                timeText.setText("Time off");
                handler.removeCallbacks(runnable);
                for (ImageView image: imageViews){
                    image.setVisibility(View.INVISIBLE);
                }

                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Restart");
                alert.setMessage("Are you sure to restart game?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=getIntent();
                        int tempScore = sharedPreferences.getInt("topScore",0);
                        if (score>tempScore) {
                            storedScore();
                        }
                        finish();
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this,"Game Over",Toast.LENGTH_LONG).show();
                    }
                });

                alert.show();

            }
        }.start();
    }

    public void storedScore () {
        sharedPreferences.edit().putInt("topScore",score).apply();
    }
}