package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadScreen extends AppCompatActivity {

    // this is an on create method that will start the xml file for the activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button= (Button)findViewById(R.id.startButton);

        //creating an on click listener to the continue button on the start screen
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = LoadScreen.this;
                Class destination = TimerActivity.class;

                //creating an intent to let the user move onto the main activity of the app
                Intent startingIntent = new Intent(context, destination);
                startActivity(startingIntent);
            }
        });
    }

}