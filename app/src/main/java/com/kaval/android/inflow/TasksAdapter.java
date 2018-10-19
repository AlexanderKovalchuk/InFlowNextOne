package com.kaval.android.inflow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;
import com.kaval.android.inflow.utils.TaskBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {


    private List<Task> tasks;
    private TaskState taskState;
    private List<Task> selected;
    private boolean selectedModeActive;
    private Context context;


    // Pass in the contact array into the constructor
    public TasksAdapter(Context context, TaskState state) {
        taskState = state;
        this.context = context;
        tasks = Task.find(Task.class, "state = ?", state.toString());
//        if (state.equals(TaskState.TODO) && tasks.size() < 30) {
//            for (int i = 0; i < 30 - tasks.size(); i++) {
//                Task task = new TaskBuilder().setName("test" + i).setDescription("hallo " + i).createTask();
//                task.save();
//                tasks.add(task);
//            }
//        }
    }

    public boolean isSelectedMODeActive() {
        return selectedModeActive;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, final int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.task_list_fragment, viewGroup, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(taskView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        // Get the data model based on position
        final Task task = tasks.get(i);

        // Set item views based on your views and data model
        viewHolder.nameTextView.setText(task.getName());
        viewHolder.messageTextView.setText(task.getDescription());
        viewHolder.duration.setText(String.valueOf(task.getDuration()));
        viewHolder.container.setBackgroundColor(task.isSelected() ? Color.GRAY : Color.WHITE);
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("", "here we are click" + "i:" + i);
                if (selectedModeActive) {
                    handleItemSelect(viewHolder, i);
                } else {
                    Intent intent = new Intent(context, AddTaskActivity.class);
                    intent.putExtra(AddTaskActivity.ADD_ACTIVITY_EDITABLE_KEY, false);
                    intent.putExtra(AddTaskActivity.TASK_TO_SHOW, task);
                    context.startActivity(intent);
                }
            }
        });
        viewHolder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                EventBus eventBus = EventBus.getDefault();
                eventBus.post(new Event.ModeChangeTaskStateEvent(getPossibleEvents()));
                if (!selectedModeActive) {
                    selectedModeActive = true;
                    selected = new ArrayList<>();
                }
                return false;
            }
        });
    }

    private List<TaskState> getPossibleEvents() {
        List<TaskState> possibleStates = new ArrayList<>();
        switch (taskState) {
            case TODO:
                possibleStates.add(TaskState.IN_PROGRESS);
                return possibleStates;
            case IN_PROGRESS:
                possibleStates.add(TaskState.TODO);
                possibleStates.add(TaskState.DONE);
                return possibleStates;
            case DONE:
                return possibleStates;
            default:
                throw new IllegalArgumentException("wrong taskState in this fragment:" + taskState);
        }
    }

    private void handleItemSelect(ViewHolder viewHolder, int position) {
        Task task = tasks.get(position);
        if (task.isSelected()) {
            task.setSelected(false);
            selected.remove(task);
        } else {
            selected.add(task);
            task.setSelected(true);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void checkForChanges() {
        tasks.clear();
        tasks.addAll(Task.find(Task.class, "state = ?", taskState.toString()));
        notifyDataSetChanged();
    }

    public void moveSelected(TaskState state) {
        for (Task task : selected) {
            task.setState(state);
            task.setSelected(false);
            task.save();
        }
        tasks.removeAll(selected);
        selected = null;
        selectedModeActive = false;
        notifyDataSetChanged();
    }

    public void cancelSelection() {
        for (Task task : selected) {
            task.setSelected(false);
        }
        selected = null;
        selectedModeActive = false;
        notifyDataSetChanged();
    }

    public void deleteSelected() {
        for (Task task : selected) {
            tasks.remove(task);
            task.delete();
        }
        selected = null;
        selectedModeActive = false;
        notifyDataSetChanged();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public TextView messageTextView;
        public LinearLayout container;
        public TextView duration;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = itemView.findViewById(R.id.task_name);
            messageTextView = itemView.findViewById(R.id.task_description);
            container = itemView.findViewById(R.id.task_item_container);
            duration = itemView.findViewById(R.id.task_duration);
        }
    }
}
