package com.example.myapplication;

import static java.lang.Integer.parseInt;
import static java.lang.Long.parseLong;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * The class for the second activity that displays a recycler view of timers via the RecyclerAdaptor class
 */
public class TimerActivity extends AppCompatActivity{

    public List<Model> item_list = new ArrayList<>();
    private RecyclerView my_recycler;
    private RecyclingAdaptor card_adapter;


    /**
     * This is an on create method that will start the xml file for the activity
     * @param savedInstanceState the previous state of the activity (used for checking intents)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        my_recycler = findViewById(R.id.idRecyclerViewS);
        my_recycler.setLayoutManager(new LinearLayoutManager(this));
        Context context = this;
        card_adapter = new RecyclingAdaptor(item_list, TimerActivity.this);
        my_recycler.setAdapter(card_adapter);

        //restores data from previous sessions
        try {
            onRestoreData();
        } catch (Exception e) { }

        //adds the timer returned by the create new timer screen while
        //checking to see if it has already been added
        if (savedInstanceState == null) {
            try {
                //grabs the intent passed to it
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                //adding bundle data to the intent
                String new_timer_name = extras.getString("NAME");
                String new_timer_length = extras.getString("LENGTH");
                Integer new_timer_time = parseInt(new_timer_length);
                card_adapter.addItem(new Model(new_timer_name,  new_timer_time, context, card_adapter));
            }
            catch(Exception e) {
            }
        }
    }

    /**
     * filters the recycler view to display only timers with a name containing some Text
     * @param text the text entered in the searchView
     */
    private void filter(String text) {
        // creating a new array list to filter our data.
        List<Model> filtered_list = new ArrayList<Model>();

        // running a for loop to compare elements.
        for (Model item : item_list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getTimerName().toLowerCase().contains(text.toLowerCase())) {
                // adding it to our filtered list.
                filtered_list.add(item);
            }
        }
        if (filtered_list.isEmpty()) {
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }
        // passes list to our adapter class.
        card_adapter.filterList(filtered_list);
    }

    /**
     * Creates the menu fo the Second Activity
     * @param menu the options menu for this page
     * @return boolean of menu created
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        //Code to retrieve text on text change and filter the recycler view
        final SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String new_text) {
                filter(new_text);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }
        };

        searchView.setOnQueryTextListener(queryTextListener);
        return true;
    }

    /**
     * Implements code for each item in the options menu
     * @param item the menu item that was clicked
     * @return boolean to see if it ran a case
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            //Sends a Intent to pass the user on to the timer creation activity
            case R.id.action_add:
                Context context = TimerActivity.this;
                Class destination = AddNewTimer.class;
                Intent starting_intent = new Intent(context, destination);
                startActivity(starting_intent);
                return true;

            //Case for the search icon
            case R.id.app_bar_search:
                return true;

            //Sends a Intent to pass the user on to the Connect Account activity (Inprogress)
            case R.id.connect_menu:
                Context connect_context = TimerActivity.this;
                Class destination_connect = ToBeContinued.class;
                Intent connect_intent = new Intent(connect_context, destination_connect);
                startActivity(connect_intent);
                return true;

            case R.id.website:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://runelite.net/"));
                startActivity(intent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    //Aaves the current list of timers

    /**
     * Saves the current list of timers into a shared preference to be re opened later
     */
    public void onSave() {
        //creates a shared preference to store the data
        SharedPreferences shared_pref = getSharedPreferences("application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared_pref.edit();
        String item_string = "";
        //for loop to iteratively add the data to the preference
        for (Model item : item_list) {
            item_string = item_string + "," + item.getTimerName() + "#" +  item.time_left + "~" + item.TOTAL_TIME/1000/60;
        }
        editor.putString("data", item_string);
        editor.apply();
    }

    /**
     * Used to retrieve the data from the previous session
     */
    public void onRestoreData() {
        //gets the shared preference
        SharedPreferences shared_pref = getSharedPreferences("application", Context.MODE_PRIVATE);
        String data = shared_pref.getString("data", "");
        data = data.substring(1);
        if (data != null) {
            String[] string_array = data.split(",",0);
            for (String timer_string : string_array) {
                if (timer_string != null || timer_string != "" ){
                    String[] timer_name_array = timer_string.split("#",0);
                    String[] timer_time_array = timer_name_array[1].split("~",0);
                    //creating the timers and adding them to the adapter
                    int time_left = parseInt(timer_time_array[0]);
                    int TOTAL_TIME = parseInt(timer_time_array[1]);
                    String name = timer_name_array[0];
                    Model model = new Model(name,  TOTAL_TIME, this, card_adapter);
                    model.setTimeLeft(time_left);
                    card_adapter.addItem(model);
                }
            }
        }
    }

    /**
     * pauses all timers in the programme
     */
    public void pauseTimers() {
        for (Model item : item_list) {
            item.pauseTimer();
        }
    }

    /**
     * Saves the data
     */
    @Override
    public void onPause() {
        pauseTimers();
        onSave();
        super.onPause();
    }
}