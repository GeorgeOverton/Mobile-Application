package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Class for the recycler adaptor that will allow the activity to display timer cards
 */
public class RecyclingAdaptor extends RecyclerView.Adapter<RecyclingAdaptor.Viewholder> {

    private Context context;
    private List<Model> user_timers;
    // new CourseAdapter
    AdapterView.OnItemClickListener on_item_click_listener;
    Model item;

    /**
     * Constructor for the adaptor
     */
    public RecyclingAdaptor(List<Model> user_timers, TimerActivity activity) {
        this.context = context;
        this.user_timers = user_timers;
        this.on_item_click_listener = on_item_click_listener;
    }

    /**
     * Adds an element to the recycler view
     * @param model A new timer
     */
    public void addItem(Model model) {
        user_timers.add(model);
    }

    /**
     * Filters the recycler view to display the timers which name contains the the text entered
     * @param filter_list the filtered list
     */
    public void filterList(List<Model> filter_list) {
        //Sets the views current list to display the filtered list
        user_timers = filter_list;
        //notifis the UI to update
        notifyDataSetChanged();
    }

    /**
     * creates a viewholder to contain the cards in the recycler view
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param view_type The view type of the new View.
     *
     * @return the Viewholder that was create
     */
    @NonNull
    public RecyclingAdaptor.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int view_type){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclingtimers
                , parent, false);
        Viewholder holder = new Viewholder(view);
        return holder;
    }

    /**
     * Binds the data and functionality of a Timer to a view holder
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclingAdaptor.Viewholder holder, int position) {

        final Model user_timer_list = user_timers.get(position);
        //Sets the values of the text fields
        holder.timer_name.setText(user_timer_list.getTimerName());
        holder.time_left.setText("Time Left");

        holder.time_left_num.setText(user_timer_list.getTimeLeft());
        holder.progress_bar.setText("Progress Bar");
        holder.progress.setProgress((int) user_timer_list.getProgress());

        //sets a onClick listener to allow a user to delete a item in the recycler view
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_timers.remove(user_timer_list);
                notifyDataSetChanged();
            }
        });

        //Check to see if the timer is running and if so set the icon accordingly
        if (user_timer_list.getTimerState()) {
            holder.play_pause.setImageResource(R.drawable.pause_button);
        } else {
            holder.play_pause.setImageResource(R.drawable.play_button);
        }

        // Sets a on click listener to the play button to start/ pause the timer
        holder.play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean timer_state = user_timer_list.getTimerState();
                //checks the current state of the timer
                if (timer_state) {
                    user_timer_list.pauseTimer();
                    holder.play_pause.setImageResource(R.drawable.play_button);
                } else {
                    user_timer_list.startTimer();
                    holder.play_pause.setImageResource(R.drawable.pause_button);
                }
            }
        });

        //Resets the timer
        holder.reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_timer_list.resetTimer();
                holder.play_pause.setImageResource(R.drawable.play_button);
                notifyDataSetChanged();
            }
        });
    }


    /**
     * Gets the number of timers in the recycler view
     * @return int number of timers
     */
    @Override
    public int getItemCount() {
        return user_timers.size();
    }


    /**
     * Class to make the recycler view more efficient by implementing view holders
     */
    public class Viewholder extends RecyclerView.ViewHolder {

        private TextView timer_name;
        private TextView time_left;
        private TextView time_left_num;
        private TextView progress_bar;
        private ProgressBar progress;
        private ImageButton play_pause;
        private ImageButton reset;

        private View view;


        /**
         * constructor method for the view holder
         * @param itemView the timer view
         */
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            timer_name = itemView.findViewById(R.id.timerName);
            time_left = itemView.findViewById(R.id.timeLeft);
            time_left_num = itemView.findViewById(R.id.timeLeftNum);
            progress_bar = itemView.findViewById(R.id.progressBar);
            progress = itemView.findViewById(R.id.progress);
            play_pause = itemView.findViewById(R.id.play_button);
            reset = itemView.findViewById(R.id.play_reset);
            view = itemView;
        }
    }
}
