package com.kaval.android.inflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class TaskListFragment extends Fragment {

    private final TaskState state;
    TasksAdapter adapter;

    public TaskListFragment(TaskState state) {
        this.state = state;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
//
//    public static String getFormattedDateFromTimeStamp(long timestampInMilliSeconds) {
//        Calendar cl = Calendar.getInstance();
//        cl.setTimeInMillis(timestampInMilliSeconds);  //here your time in miliseconds
//        return "" + cl.get(Calendar.DAY_OF_MONTH) + ":" + cl.get(Calendar.MONTH) + ":" + cl.get(Calendar.YEAR) + "  " + cl.get(Calendar.HOUR_OF_DAY) + ":" + cl.get(Calendar.MINUTE) + ":" + cl.get(Calendar.SECOND);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // ...
        // Lookup the recyclerview in activity layout
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView rvTasks = view.findViewById(R.id.rvtasks);

        // Create adapter passing in the sample user data
        adapter = new TasksAdapter(state);
        // Attach the adapter to the recyclerview to populate items
        rvTasks.setAdapter(adapter);
        rvTasks.setHasFixedSize(true);
        // Set layout manager to position the items
        rvTasks.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

    }

    public void updateAdapter() {
        adapter.checkForChanges();
    }

}

class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {


    private List<Task> tasks;
    private TaskState taskState;

    // Pass in the contact array into the constructor
    public TasksAdapter(TaskState state) {
        taskState = state;
        tasks = Task.listAll(Task.class);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.task_list_fragment, viewGroup, false);
        taskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        // Get the data model based on position
        Task task = tasks.get(i);

        // Set item views based on your views and data model
        TextView name = viewHolder.nameTextView;
        name.setText(task.getName());
        TextView descr = viewHolder.messageTextView;
        descr.setText(task.getDescription());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void checkForChanges() {
        tasks.clear();
        tasks.addAll(Task.listAll(Task.class));
        notifyDataSetChanged();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView messageTextView;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.task_name);
            messageTextView = (TextView) itemView.findViewById(R.id.task_description);

        }
    }
}
