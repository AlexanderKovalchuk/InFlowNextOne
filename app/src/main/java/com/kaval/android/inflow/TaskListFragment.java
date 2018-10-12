package com.kaval.android.inflow;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kaval.android.inflow.Enums.TaskState;
import com.kaval.android.inflow.model.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@SuppressLint("ValidFragment")
public class TaskListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final TaskState state;
    private TasksAdapter adapter;
    private SwipeRefreshLayout refreshLayout;


    public TaskListFragment(TaskState state) {
        this.state = state;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.MoveCurrentSelectedItemEvent event) {
        adapter.moveSelected(event.state);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.DeleteCurrentSelectedItemEvent event) {
        adapter.deleteSelected();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event.ChangeSelectedTabEvent event) {
        if(!event.taskState.equals(state)){
            adapter.cancelSelection();
        }
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
        refreshLayout = view.findViewById(R.id.swipe_container);
        refreshLayout.setOnRefreshListener(this);
        // Create adapter passing in the sample user data
        adapter = new TasksAdapter(getContext(), state);
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

    @Override
    public void onRefresh() {
        updateAdapter();
        refreshLayout.setRefreshing(false);
    }

    public boolean onBackPressed() {
        if(adapter.isSelectedMODeActive()){
            adapter.cancelSelection();
            return true;
        }
        return false;
    }
}


