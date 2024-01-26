package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ToBeContinued extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_continued);
        Button button= (Button)findViewById(R.id.tbcButton);

        //creating an on click listener to the continue button on the start screen
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = ToBeContinued.this;
                Class destination = TimerActivity.class;

                //creating an intent to let the user move onto the main activity of the app
                Intent startingIntent = new Intent(context, destination);
                startActivity(startingIntent);
            }
        });
    }
}