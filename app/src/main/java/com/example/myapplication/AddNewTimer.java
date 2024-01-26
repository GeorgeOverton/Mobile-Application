package com.example.myapplication;

import static java.lang.Integer.parseInt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class to create the activity that allows a user to create new timers
 */
public class AddNewTimer extends AppCompatActivity {

    EditText timer_name;

    EditText timer_length;

    /**
     * A onCreate method for the activity that sets up some text Views and the button onClick
     * @param savedInstanceState the state of the previous instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_timer);

        Button button= (Button)findViewById(R.id.button);

        //Runs if the screen is rotated on this screen
        try {
            onRestoreData();
        } catch (Exception e) {
            System.out.println("fatal error loading");
            System.out.println(e);
        }

        //Button Listener to add the new timers
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Context context = AddNewTimer.this;
                Class destination = TimerActivity.class;
                Bundle extras = new Bundle();

                timer_name =  (EditText) findViewById(R.id.timer_name);
                String name = timer_name.getText().toString();
                timer_length  =  (EditText) findViewById(R.id.timer_length);
                String length = timer_length.getText().toString();

                extras.putString("NAME",name);
                extras.putString("LENGTH", length);

                Intent startingIntent = new Intent(context, destination);
                startingIntent.putExtras(extras);
                startActivity(startingIntent);
            }
        });
    }

    /**
     * Saves the data in the page into a shared prefrence
     */
    public void onSave() {
        SharedPreferences sharedPref = getSharedPreferences("creating_model", this.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String timer_name_save = "";
        String timer_length_save = "";
        try {
            timer_name_save = timer_name.getText().toString();
        }catch (Exception e) {}
        try {
            timer_length_save = timer_length.getText().toString();
        }catch (Exception e) {}
        String item_string = timer_name_save + "," + timer_length_save;
        editor.putString("data", item_string);
        editor.apply();
        System.out.println(item_string);
    }

    /**
     * Gets the data from the shared prefrence
     */
    public void onRestoreData() {
        SharedPreferences sharedPref = getSharedPreferences("creating_model", this.MODE_PRIVATE);
        String data = sharedPref.getString("data", "");
        data = data.substring(2);
        if (data != null) {
            String[] stringArray = data.split(",",0);
            System.out.println(data);
            timer_name.setText(stringArray[0]);
            timer_length.setText(stringArray[1]);
        }
    }

    @Override
    public void onPause() {
        onSave();
        super.onPause();
    }
}